package com.example.cafelabservice.entity

import jakarta.persistence.CollectionTable
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.ElementCollection
import jakarta.persistence.Index
import jakarta.persistence.ManyToMany
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "orders")
data class Order(
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0,
    val orderPlacedTimestamp: LocalDateTime = LocalDateTime.now(),
    val orderPaidTimestamp: LocalDateTime? = null,
    val status: String = "",
    @ManyToMany
    val products: List<Product> = listOf(),
    val totalPaid: Double = 0.0,
    val isSubscription: Boolean = false
)