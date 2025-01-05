package com.example.cafelabservice.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "events")
data class Event(
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0,
    val name: String = "",
    val description: String = "",
    val location: String = "",
    val date: LocalDateTime = LocalDateTime.now(),
)