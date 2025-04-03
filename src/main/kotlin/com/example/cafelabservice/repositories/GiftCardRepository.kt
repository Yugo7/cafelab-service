package com.example.cafelabservice.repositories

import com.example.cafelabservice.entity.GiftCard
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface GiftCardRepository: JpaRepository<GiftCard, Long> {
    fun findByCode(code: String): GiftCard?

    fun existsByCode(code: String): Boolean
}