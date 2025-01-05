package com.example.cafelabservice.controllers

import com.example.cafelabservice.entity.Event
import com.example.cafelabservice.service.EventsService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/events")
class EventsController(private val eventsService: EventsService) {

    @GetMapping
    fun getAllEvents() = eventsService.getAllEvents()

    @GetMapping("/{id}")
    fun getEventById(@PathVariable id: Long) = eventsService.getEventById(id)

    @PostMapping
    fun createEvent(@RequestBody event: Event) = eventsService.createEvent(event)

    @PutMapping("/{id}")
    fun updateEvent(@PathVariable id: Long, @RequestBody newEvent: Event) {
        eventsService.updateEvent(id, newEvent)
    }

    @DeleteMapping("/{id}")
    fun deleteEvent(@PathVariable id: Long) = eventsService.deleteEvent(id)
}