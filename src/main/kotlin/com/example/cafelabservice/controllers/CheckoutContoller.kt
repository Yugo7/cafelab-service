package com.example.cafelabservice.controllers

import com.example.cafelabservice.service.CheckoutService
import com.example.cafelabservice.models.dto.OrderRequestDTO
import com.example.cafelabservice.models.dto.SessionDTO
import com.example.cafelabservice.models.dto.checkout.SubscriptionRequestDTO
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/checkout")
class CheckoutContoller(
    private val checkoutService: CheckoutService,
) {

    @PostMapping("/subscription")
    @ResponseStatus(HttpStatus.OK)
    fun checkoutSubscription(@RequestBody request: SubscriptionRequestDTO) = SessionDTO.fromSession(checkoutService.generateCheckoutSessionForSubscriptions(request))

    @PostMapping("/payment")
    @ResponseStatus(HttpStatus.OK)
    fun checkout(@RequestBody request: OrderRequestDTO) = SessionDTO.fromSession(checkoutService.generateCheckoutSession(request))
}