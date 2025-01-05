package com.example.cafelabservice.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Table

@Entity
@Table(name = "users")
data class User(
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0,
    val username: String = "",
    val firstName: String = "",
    val middleNames: String? = "",
    val lastName: String = "",
    val email: String = "",
    val address: String = "",
    val password: String = ""
)