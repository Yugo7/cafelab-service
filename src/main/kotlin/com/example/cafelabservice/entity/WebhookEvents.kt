package com.example.cafelabservice.entity

import com.example.cafelabservice.converter.JsonbListConverter
import jakarta.persistence.*
import org.hibernate.annotations.ColumnTransformer
import java.time.LocalDateTime

@Entity
@Table(name = "webhook_events")
data class WebhookEvent(
    @Id
    val id: String,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "data", columnDefinition = "jsonb")
    @ColumnTransformer(write = "?::jsonb")
    val data: String? = null,

    @Column(name = "type")
    val type: String? = null,

    @Column(name = "event_type_id")
    val eventTypeId: String? = null
)