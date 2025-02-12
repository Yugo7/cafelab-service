package com.example.cafelabservice.entity

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "newsletter")
data class MailMarketing(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @JsonProperty("created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val name: String? = null,
    @Column(nullable = false, unique = true)
    val email: String,

    @JsonProperty("is_active")
    val isActive: Boolean = true
)