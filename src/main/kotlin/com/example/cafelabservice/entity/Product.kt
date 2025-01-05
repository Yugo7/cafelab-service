package com.example.cafelabservice.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "products")
data class Product(
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0,
    val name: String = "",
    val description: String = "",
    val brand: String = "",
    val countryOfOrigin: String = "",
    val weight: Double? = null,
    val price: Double = 0.0,
    val cost: Double = 0.0,
    val available: Boolean = false,
    val leastQuantityInStock: Int = 0,
    val firstDateAvailable: LocalDateTime = LocalDateTime.now()
)