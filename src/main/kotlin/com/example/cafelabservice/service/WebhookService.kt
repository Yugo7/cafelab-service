package com.example.cafelabservice.service

import com.example.cafelabservice.entity.WebhookEvent
import com.example.cafelabservice.repositories.WebhookEventsRepository
import com.stripe.exception.SignatureVerificationException
import com.stripe.model.Event
import com.stripe.model.PaymentIntent
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
    private val emailService: EmailService
) {
    private val logger: Logger = LoggerFactory.getLogger(WebhookService::class.java)

    fun handleWebhook(event: Event) {
        try {
            when (event.type) {
                "payment_intent.succeeded" -> handlePaymentIntentSucceeded(event)
                "payment_intent.payment_failed" -> handlePaymentIntentFailed(event)
                "checkout.session.completed" -> handleCheckoutSessionCompleted(event)

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
        requireNotNull(stripeEventObject) { "Stripe event object is null" }

        saveEvent(event.type, stripeEventObject.toJson(), event.id, stripeEventObject.id)
        println("PaymentIntent was successful! $stripeEventObject")
    }

    private fun handlePaymentIntentFailed(event: Event) {
        // Handle the event
        println("PaymentIntent failed!")
    }

    private fun handleCheckoutSessionCompleted(event: Event) {
        logger.info("Checkout $event. was completed!")
        val stripeEventObject = event.dataObjectDeserializer.`object`.orElse(null) as
                Session?

        requireNotNull(stripeEventObject) { "Stripe Session event object is null" }

        val invoice = webhookEventsRepository.getEventByEventTypeIdAndType(stripeEventObject.invoice, "invoice.payment_succeeded")
        val stripeInvoice = invoice.let {
            stripeService.getInvoice(stripeEventObject.invoice)
        }

        val updatedOrder = stripeEventObject.metadata.get("order_id")?.let { orderId ->
            val pdfUrl = runBlocking {
                downloadService.downloadAndUploadPdf(stripeInvoice.invoicePdf, orderId)
            }

            orderService.updateFromCheckoutSessionCompleted(
                orderId.toLong(),
                pdfUrl,
                stripeInvoice.total.toString(),
                stripeEventObject.customerEmail,
                stripeEventObject.customFields.get(0).key,
                stripeEventObject.id
            )
        }

        require(updatedOrder != null) { "There was an error updating order ${stripeEventObject.id}" }
        emailService.sendOrderConfirmationEmail(stripeEventObject.customerEmail, updatedOrder)
        //create guest or update user

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