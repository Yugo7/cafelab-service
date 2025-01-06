package com.example.cafelabservice.entity

import jakarta.persistence.*
import java.time.ZonedDateTime

@Entity
@Table(name = "\"order\"")
data class Order(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @Column(columnDefinition = "jsonb")
    val products: String? = null,
    val total: Double? = null,
    val status: String? = null,
    val userId: String? = null,
    @Column(name = "\"user\"", columnDefinition = "jsonb")
    val user: String? = null,
    val userStripeId: String? = null,
    val sessionId: String? = null,
    val createdAt: ZonedDateTime = ZonedDateTime.now(),
    val receiptUrl: String? = null,
    val variety: String? = null,
    val note: String? = null,
    @Enumerated(EnumType.STRING)
    val type: OrderType = OrderType.LOJA,
    val isTest: Boolean = false
)

enum class OrderType {
    LOJA, SUBSCRICAO, BOUTIQUE // Add other types if needed
}