package com.example.cafelabservice.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "email_log")
data class EmailLog(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @Column(name = "\"from\"")
    val from: String,
    @Column(name = "\"to\"")
    val to: String,
    val subject: String,
    val content: String,
    val sentAt: LocalDateTime = LocalDateTime.now()
)