package com.example.cafelabservice.controllers.v1

import com.example.cafelabservice.service.WebhookService
import com.google.gson.JsonSyntaxException
import com.stripe.model.Event
import com.stripe.net.ApiResource
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/webhooks")
class WebhooksController(
    private val webhookService: WebhookService
) {

    @PostMapping
    fun getWebhooks(@RequestBody payload: String, @RequestHeader("Stripe-Signature") sigHeader: String): ResponseEntity<String> {
        try {
            webhookService.handleWebhook(ApiResource.GSON.fromJson(payload, Event::class.java))
        } catch (e: JsonSyntaxException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.localizedMessage)
        }

        return ResponseEntity.status(HttpStatus.OK).body(null)
    }
}