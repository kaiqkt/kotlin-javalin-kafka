package com.kaique.domain.services

import com.kaique.domain.entities.Event
import com.kaique.resources.kafka.EventProducer

class EmitEventService(private val eventProducer: EventProducer) {

    fun send(event: Event<String>, correlationId: String?) {
        eventProducer.emit(event, correlationId)
    }
}