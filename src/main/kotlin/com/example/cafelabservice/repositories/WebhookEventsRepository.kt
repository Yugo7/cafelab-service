package com.example.cafelabservice.repositories

import com.example.cafelabservice.entity.WebhookEvent
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface WebhookEventsRepository: JpaRepository<WebhookEvent, Long> {
    fun getEventByEventTypeIdAndType(eventTypeId: String, type: String): WebhookEvent?
}