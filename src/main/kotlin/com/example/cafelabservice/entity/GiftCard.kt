package com.example.cafelabservice.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "gift_cards")
data class GiftCard(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(unique = true, nullable = false)
    val code: String,

    @Column(nullable = false)
    val amount: Long,

    @Column(nullable = false)
    var balance: Long,

    @Column(nullable = false)
    var isRedeemed: Boolean = false,

    @ManyToOne
    @JoinColumn(name = "redeemed_by_user_id")
    var redeemedBy: User? = null,

    val expirationDate: LocalDateTime = LocalDateTime.now().plusMonths(12)
)