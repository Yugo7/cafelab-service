package com.example.cafelabservice.service

import com.example.cafelabservice.entity.Event
import com.example.cafelabservice.entity.Order
import com.example.cafelabservice.repositories.EventsRepository
import com.example.cafelabservice.repositories.OrderRepository
import org.springframework.stereotype.Service

@Service
class EventsService(private val eventRepository: EventsRepository) {

    fun getAllEvents() = eventRepository.findAll()

    fun getEventById(id: Long) = eventRepository.findById(id)

    fun createEvent(event: Event) = eventRepository.save(event)

    fun updateEvent(id: Long, newEvent: Event) {
        eventRepository.findById(id).map { existingEvent ->
            val updatedEvent: Event = existingEvent
                .copy(
                    name = newEvent.name,
                    description = newEvent.description,
                    location = newEvent.location,
                    date = newEvent.date
                )
            eventRepository.save(updatedEvent)
        }.orElseThrow { NoSuchElementException("No Event found with id $id") }
    }

    fun deleteEvent(id: Long) = eventRepository.deleteById(id)
}