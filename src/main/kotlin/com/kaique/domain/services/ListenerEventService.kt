package com.kaique.domain.services

import com.kaique.domain.entities.Event
import com.kaique.domain.gateways.EventRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ListenerEventService(private val eventRepository: EventRepository) {

    fun listener(event: Event<String>) {
        eventRepository.create(event)
    }
}