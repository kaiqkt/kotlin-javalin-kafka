package com.kaique.resources.repositories.documents

import com.kaique.domain.entities.Event
import io.azam.ulidj.ULID

data class EventDocument(
    val _id: String,
    val body: String
) {
    companion object {
        fun fromTransfer(event: Event<String>): EventDocument = EventDocument(
            _id = ULID.random(),
            body = event.payload
        )
    }
}