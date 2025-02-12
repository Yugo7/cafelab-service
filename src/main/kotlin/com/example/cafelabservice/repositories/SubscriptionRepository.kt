package com.example.cafelabservice.repositories

import com.example.cafelabservice.entity.Subscription
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SubscriptionRepository: JpaRepository<Subscription, Long> {
}