package com.kaique.domain.gateways

import com.kaique.domain.entities.Event

interface EventRepository {
    fun create(event: Event<String>)
}