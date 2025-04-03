package com.example.cafelabservice.service.handlers

import com.stripe.model.Event
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class PaymentSucceededHandler: WebhookEventHandler {
    private val logger = LoggerFactory.getLogger(PaymentSucceededHandler::class.java)
    override val eventKey: String = "payment_intent.succeeded"

    override fun handle(event: Event): Boolean {
        logger.info("Payment succeeded event received")
        return true
    }
}