package com.example.cafelabservice.entity

import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "analytics")
data class Analytics(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "date")
    val date: LocalDate? = null,

    @Column(name = "accesses", nullable = false)
    val accesses: Long = 0,

    @Column(name = "visitors", nullable = false)
    val visitors: Long = 0,

    @Column(name = "bounce_rate", nullable = false)
    val bounceRate: Int = 0
)