package com.example.cafelabservice.service

import com.example.cafelabservice.entity.WebhookEvent
import com.example.cafelabservice.exception.StripeWebhookPayloadException
import com.example.cafelabservice.models.ShippingInfo
import com.example.cafelabservice.repositories.WebhookEventsRepository
import com.example.cafelabservice.service.handlers.WebhookEventHandler
import com.stripe.exception.SignatureVerificationException
import com.stripe.model.Event
import com.stripe.model.Invoice
import com.stripe.model.PaymentIntent
import com.stripe.model.Subscription
import com.stripe.model.checkout.Session
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class WebhookService(
    private val webhookEventsRepository: WebhookEventsRepository,
    private val stripeService: StripeService,
    private val downloadService: DownloadService,
    private val orderService: OrderService,
    private val emailService: EmailService,
    handlers: List<WebhookEventHandler>
) {
    private val logger: Logger = LoggerFactory.getLogger(WebhookService::class.java)

    private val handlerMap: Map<String, WebhookEventHandler> = handlers.associateBy { it::eventKey.get() }

    fun process(event: Event) {
        val handler = handlerMap[event.type]

        try {
            handler?.handle(event) ?: println("No handler found for event: ${event.type}")
        } catch (e: SignatureVerificationException) {
            println("Invalid signature")
        }
    }

    fun handleWebhook(event: Event) {
        try {
            when (event.type) {
                "payment_intent.succeeded" -> handlePaymentIntentSucceeded(event)
                "payment_intent.payment_failed" -> handlePaymentIntentFailed(event)
                "checkout.session.completed" -> handleCheckoutSessionCompleted(event)
                "invoice.payment_succeeded" -> {
                    val invoice = event.dataObjectDeserializer.`object`.orElse(null) as? Invoice
                    invoice?.let {
                        println("Payment succeeded: ${it.id}")
                        // Grant access to user
                    }
                }

                "invoice.payment_failed" -> {
                    val invoice = event.dataObjectDeserializer.`object`.orElse(null) as? Invoice
                    invoice?.let {
                        println("Payment failed: ${it.id}")
                        // Notify user to update payment
                    }
                }

                "customer.subscription.updated" -> {
                    val subscription = event.dataObjectDeserializer.`object`.orElse(null) as? Subscription
                    subscription?.let {
                        println("Subscription updated: ${it.id}")
                        // Handle upgrade/downgrade
                    }
                }

                "customer.subscription.deleted" -> {
                    val subscription = event.dataObjectDeserializer.`object`.orElse(null) as? Subscription
                    subscription?.let {
                        println("Subscription canceled: ${it.id}")
                        // Revoke access to user
                    }
                }

                else -> println("Unhandled event type: ${event.type}")
            }
        } catch (e: SignatureVerificationException) {
            // Invalid signature
            println("Invalid signature")
        }
    }

    private fun handlePaymentIntentSucceeded(event: Event) {
        val stripeEventObject = event.dataObjectDeserializer.`object`.orElse(null) as
                PaymentIntent?

        requireNotNull(stripeEventObject) { throw StripeWebhookPayloadException("Stripe PaymentIntent event object is null") }

        saveEvent(event.type, stripeEventObject.toJson(), event.id, stripeEventObject.id)
        println("PaymentIntent was successful! $stripeEventObject")
    }

    private fun handlePaymentIntentFailed(event: Event) {
        // Handle the event
        println("PaymentIntent failed!")
    }

    private fun handleCheckoutSessionCompleted(event: Event) {
        logger.info("Checkout ${event.id} was completed!")
        val stripeEventObject = event.dataObjectDeserializer.`object`.orElse(null) as
                Session?

        requireNotNull(stripeEventObject) { throw StripeWebhookPayloadException("Stripe Session event object is null") }

        val shippingInfo = stripeEventObject.shippingDetails?.address?.let {
            ShippingInfo(
                address = it.line1,
                line2 = it.line2,
                city = it.city,
                zip = it.postalCode,
                state = it.state,
                country = it.country,
                gift = false,
                shippingCost = stripeEventObject.shippingCost?.amountTotal?.toString()
            )
        }

        val invoice =
            webhookEventsRepository.getEventByEventTypeIdAndType(stripeEventObject.invoice, "invoice.payment_succeeded")
        val stripeInvoice = invoice.let {
            stripeService.getInvoice(stripeEventObject.invoice)
        }

        val updatedOrder = stripeEventObject.metadata.get("order_id")?.let { orderId ->
            val pdfData = runBlocking {
                downloadService.downloadPdf(stripeInvoice.invoicePdf)
            }

            val pdfUrl = pdfData?.let {
                runBlocking {
                    downloadService.uploadPdf(it, orderId)
                }
            }

            orderService.updateFromCheckoutSessionCompleted(
                orderId.toLong(),
                pdfUrl,
                stripeInvoice.total.toString(),
                stripeEventObject.customerEmail ?: stripeEventObject.customerDetails.email
                ?: throw StripeWebhookPayloadException("No email found"),
                stripeEventObject.customFields.get(0).text.value,
                stripeEventObject.id
            )?.let {
                emailService.sendOrderConfirmationEmail(it.userId!!, it, shippingInfo, pdfData)
            }
        }

        requireNotNull(updatedOrder) { StripeWebhookPayloadException("There was an error updating order ${stripeEventObject.id}") }
        println("Checkout session completed!")
    }

    private fun saveEvent(type: String, data: String, eventId: String, eventTypeId: String) {
        val webhookEvent = WebhookEvent(
            type = type,
            data = data,
            id = eventId,
            eventTypeId = eventTypeId
        )
        webhookEventsRepository.save(webhookEvent)
    }
}