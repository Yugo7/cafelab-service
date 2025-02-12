package com.example.cafelabservice.service

import com.example.cafelabservice.entity.Subscription
import com.example.cafelabservice.repositories.SubscriptionRepository
import org.springframework.stereotype.Service

@Service
class SubscriptionService(
    private val subscriptionRepository: SubscriptionRepository
) {
    fun getAllSubscriptions() = subscriptionRepository.findAll()

    fun getSubscriptionById(id: Long) = subscriptionRepository.findById(id)

    fun createSubscription(subscription: Subscription) = subscriptionRepository.save(subscription)

    fun updateSubscription(id: Long, newSubscription: Subscription) {
        subscriptionRepository.findById(id).map { existingSubscription ->
            val updatedSubscription: Subscription = existingSubscription
                .copy(
                    name = newSubscription.name,
                    price = newSubscription.price,
                    description = newSubscription.description
                )
            subscriptionRepository.save(updatedSubscription)
        }
    }

    fun deleteSubscription(id: Long) = subscriptionRepository.deleteById(id)
}