package com.example.cafelabservice.models

import com.example.cafelabservice.entity.Order
import com.example.cafelabservice.entity.Product
import com.example.cafelabservice.models.enums.OrderStatus
import com.example.cafelabservice.models.enums.OrderType
import com.fasterxml.jackson.databind.ObjectMapper
import java.time.ZonedDateTime

data class OrderToCreate(
    val id: Long = 0,
    val orderProducts: Map<Product, Int>,
    val total: Double? = null,
    val status: OrderStatus? = null,
    val userId: String? = null,
    val user: Int? = null,
    val userStripeId: String? = null,
    val sessionId: String? = null,
    val createdAt: ZonedDateTime = ZonedDateTime.now(),
    val receiptUrl: String? = null,
    val variety: String? = null,
    val note: String? = null,
    val type: OrderType,
    val isTest: Boolean = false,
    private val objectMapper: ObjectMapper
) {
    fun toOrder(): Order {
        return Order(
            id = id,
            orderProducts = orderProducts.map { (product, quantity) ->
                OrderProduct(
                    productId = product.id,
                    quantity = quantity
                )
            },
            total = total?.let { String.format("%.2f", it) } ?: "0.00",
            status = status ?: OrderStatus.PENDING,
            userId = userId,
            user = user,
            userStripeId = userStripeId,
            sessionId = sessionId,
            createdAt = createdAt,
            receiptUrl = receiptUrl,
            variety = variety,
            note = note,
            type = type,
            isTest = isTest
        )
    }
}
