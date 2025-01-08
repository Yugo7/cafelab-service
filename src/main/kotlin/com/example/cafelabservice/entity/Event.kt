package com.example.cafelabservice.entity

import jakarta.persistence.*
import java.time.LocalDate
import java.time.ZonedDateTime

@Entity
@Table(name = "events")
data class Event(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val createdAt: ZonedDateTime = ZonedDateTime.now(),
    val name: String? = null,
    val description: String? = null,
    val local: String? = null,
    val date: LocalDate? = null,
    val imageFinish: String? = null,
    val imagePromotion: String? = null,
    val instagramUrl: String? = null
)