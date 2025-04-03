package com.example.cafelabservice.service.handlers

import com.stripe.model.Event
import org.slf4j.LoggerFactory

class SubscriptionUpdateHandler: WebhookEventHandler {
    private val logger = LoggerFactory.getLogger(SubscriptionUpdateHandler::class.java)
    override val eventKey: String = "customer.subscription.updated"

    override fun handle(event: Event): Boolean {
        logger.info("Subscription updated event received")
        return true
    }
}