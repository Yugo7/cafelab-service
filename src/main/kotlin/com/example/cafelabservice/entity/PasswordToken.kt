package com.example.cafelabservice.entity

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "password_tokens")
data class PasswordToken(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: UUID = UUID.randomUUID(),

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User,

    @Column(name = "expires_at")
    val expiresAt: LocalDateTime,

    @Column(name = "token")
    val token: String
)