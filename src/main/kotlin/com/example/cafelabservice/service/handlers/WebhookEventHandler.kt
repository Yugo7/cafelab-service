package com.example.cafelabservice.service.handlers

import com.stripe.model.Event
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
interface WebhookEventHandler {
    val eventKey: String
    fun handle(event: Event): Boolean

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(WebhookEventHandler::class.java)
    }
}