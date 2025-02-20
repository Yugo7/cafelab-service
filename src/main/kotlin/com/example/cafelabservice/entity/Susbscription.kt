package com.example.cafelabservice.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "subscriptions")
data class Subscription(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    val name: String? = null,
    val periodicity: Int? = null,
    val priceId: String? = null,
    val description: String? = null,
    val image: String? = null,
    val periodicityString: String? = null,
    val price: String? = null
)