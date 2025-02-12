package com.example.cafelabservice.models

import jakarta.persistence.*

@Embeddable
data class OrderProduct(
    @Column(name = "product_id", nullable = false)
    val productId: Long,

    @Column(name = "quantity", nullable = false)
    val quantity: Int
) {

}