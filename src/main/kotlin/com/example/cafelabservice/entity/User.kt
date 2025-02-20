package com.example.cafelabservice.entity

import com.example.cafelabservice.converter.JsonbListConverter
import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "\"user\"")
data class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val name: String? = null,
    val email: String,
    val birthday: LocalDate? = null,
    @Column(columnDefinition = "jsonb")
    val address: String? = null,
    val nif: String? = null,
    val username: String? = null,
    val password: String,
    @Convert(converter = JsonbListConverter::class)
    @Column(columnDefinition = "jsonb")
    val role: List<String>? = null,
    val stripeId: String? = null,
    @Column(columnDefinition = "jsonb")
    val guestStripeIds: String? = null,
    val isGuest: Boolean = false
)