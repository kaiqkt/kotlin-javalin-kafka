package com.kaique.domain.services

import com.kaique.application.web.broker.entities.Event
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class EventService {

    private val log: Logger = LoggerFactory.getLogger(javaClass)

    fun listener(event: Event<String>) {
        log.info("Event $event ")
    }
}