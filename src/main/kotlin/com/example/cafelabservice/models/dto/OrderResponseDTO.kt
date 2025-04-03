package com.example.cafelabservice.models.dto

import com.example.cafelabservice.entity.Order
import com.example.cafelabservice.models.enums.OrderType
import com.example.cafelabservice.service.ProductService
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

fun Order.toOrderResponseDTO(productService: ProductService): OrderResponseDTO {
    return OrderResponseDTO(
        id = id,
        cart = orderProducts.map {
            OrderProductToQuantity(ProductViewDTO.fromProduct(
                productService.getProductById(it.productId)
                    .orElseThrow { Exception("Product not found") }), it.quantity)
        },
        status = status.nome,
        user = user.toString(),
        createdAt = createdAt,
        receiptUrl = receiptUrl,
        grindSize = variety ?: "",
        total = total ?: "",
        note = note,
        type = type
    )
}

fun Order.toOrderResponseDTO(): OrderResponseDTO {
    return OrderResponseDTO(
        id = id,
        cart = emptyList(),
        status = status.nome,
        user = user.toString(),
        createdAt = createdAt,
        receiptUrl = receiptUrl,
        grindSize = variety ?: "",
        total = total ?: "",
        note = note,
        type = type
    )
}