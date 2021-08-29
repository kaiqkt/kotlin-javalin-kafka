package com.kaique.domain.services

import com.kaique.domain.entities.Event
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ListenerEventService {

    private val log: Logger = LoggerFactory.getLogger(javaClass)

    fun listener(event: Event<String>) {
        log.info("Event $event ")
    }
}