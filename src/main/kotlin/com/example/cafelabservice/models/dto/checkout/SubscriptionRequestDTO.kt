package com.example.cafelabservice.models.dto.checkout

import com.example.cafelabservice.entity.User
import com.example.cafelabservice.models.dto.UserDTO
import com.fasterxml.jackson.annotation.JsonProperty

data class SubscriptionRequestDTO(
    val subscription: Subscription,
    val coffees: List<Coffee>?,
    val user: UserDTO?
)

data class Subscription(
    val id: Long,
    @JsonProperty("payment")
    val billingCycle: String,
    @JsonProperty("variety")
    val grindSize: String,
)

data class Coffee (
    val id: Long,
    val name: String,
    val quantity: Int
)
