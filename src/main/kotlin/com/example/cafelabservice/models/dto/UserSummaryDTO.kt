package com.example.cafelabservice.models.dto

data class UserSummaryDTO(
    val name: String,
    val email: String,
    val orders: List<OrderResponseDTO>,
    val subscriptions: List<SubscriptionResponseDTO>?,
    val balance: UserBalanceDTO?,
)
