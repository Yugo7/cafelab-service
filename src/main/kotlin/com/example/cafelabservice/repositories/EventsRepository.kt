package com.example.cafelabservice.repositories

import com.example.cafelabservice.entity.Event
import com.example.cafelabservice.entity.Order
import org.springframework.data.jpa.repository.JpaRepository

interface EventsRepository : JpaRepository<Event, Long>