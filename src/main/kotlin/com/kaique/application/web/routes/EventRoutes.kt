package com.kaique.application.web.routes

import com.kaique.application.web.broker.entities.Event
import com.kaique.application.web.broker.producer.EventProducer
import io.javalin.apibuilder.ApiBuilder.post

fun eventRoutes(eventProducer: EventProducer) {

    post("/") { ctx ->
        val event = ctx.body<Event<String>>()
        val correlationId = ctx.header("correlationId")
        eventProducer.emit(event, correlationId)
    }
}