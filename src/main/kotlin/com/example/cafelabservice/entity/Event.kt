package com.example.cafelabservice.entity

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "events")
data class Event(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @JsonProperty("created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val name: String? = null,
    val description: String? = null,
    val local: String? = null,
    val date: LocalDate? = null,
    @JsonProperty("image_finish")
    val imageFinish: String? = null,
    @JsonProperty("image_promotion")
    val imagePromotion: String? = null,
    @JsonProperty("instagram_url")
    val instagramUrl: String? = null
)