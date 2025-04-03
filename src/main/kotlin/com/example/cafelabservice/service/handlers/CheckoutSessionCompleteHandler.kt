package com.example.cafelabservice.service.handlers

import com.example.cafelabservice.exception.StripeWebhookPayloadException
import com.example.cafelabservice.models.ShippingInfo
import com.example.cafelabservice.repositories.WebhookEventsRepository
import com.example.cafelabservice.service.*
import com.stripe.model.Event
import com.stripe.model.checkout.Session
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class CheckoutSessionCompleteHandler(
    private val webhookEventsRepository: WebhookEventsRepository,
    private val stripeService: StripeService,
    private val downloadService: DownloadService,
    private val orderService: OrderService,
    private val emailService: EmailService,
    private val giftCardService: GiftCardService
) : WebhookEventHandler {
    override val eventKey = "checkout.session.completed"
    private val logger: Logger = LoggerFactory.getLogger(CheckoutSessionCompleteHandler::class.java)

    override fun handle(event: Event): Boolean {
        logger.info("Checkout ${event.id} was completed, processing webhook")
        val stripeEventObject = event.dataObjectDeserializer.`object`.orElse(null) as Session?

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

        val invoice = webhookEventsRepository.getEventByEventTypeIdAndType(stripeEventObject.invoice, "invoice.payment_succeeded")
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
            )?.let { order ->
                val voucher = order.orderProducts.any { product -> product.productId in listOf(41L) }
                    .takeIf { it }
                    ?.let { giftCardService.createGiftCard(2500) }
                voucher?.let {
                    emailService.sendGiftCardEmail(order.userId!!, it.code, it.amount)
                }

                emailService.sendOrderConfirmationEmail(order.userId!!, order, shippingInfo, pdfData)
            }
        }

        requireNotNull(updatedOrder) { StripeWebhookPayloadException("There was an error updating order ${stripeEventObject.id}") }
        println("Checkout session completed!")
        return true
    }
}