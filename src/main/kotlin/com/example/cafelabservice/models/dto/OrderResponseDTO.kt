package com.example.cafelabservice.models.dto

import com.example.cafelabservice.models.enums.OrderType
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.ZonedDateTime

data class OrderResponseDTO(
    val id: Long,
    val status: String,
    val user: String?,
    val cart: List<OrderProductToQuantity>,
    val createdAt: ZonedDateTime,
    val receiptUrl: String? = null,
    @JsonProperty("variety")
    val grindSize: String,
    val note: String? = null,
    val type: OrderType,
    val total: String
)

data class OrderProductToQuantity(
    val product: ProductViewDTO,
    val quantity: Int
){
    companion object {
        fun from(product: ProductViewDTO, quantity: Int): OrderProductToQuantity {
            return OrderProductToQuantity(product, quantity)
        }
    }
}