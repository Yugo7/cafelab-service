package com.example.cafelabservice.models.dto

import com.example.cafelabservice.models.enums.OrderType
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.ZonedDateTime

data class OrderResponseDTO(
    val id: Long,
    val status: String,
    val user: String?,
    val products: List<ProductViewDTO>,
    val createdAt: ZonedDateTime,
    val receiptUrl: String? = null,
    @JsonProperty("variety")
    val grindSize: String,
    val note: String? = null,
    val type: OrderType,
)