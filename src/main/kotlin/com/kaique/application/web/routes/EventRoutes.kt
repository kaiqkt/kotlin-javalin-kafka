package com.kaique.application.web.routes

import com.kaique.application.web.broker.entities.Event
import com.kaique.application.web.broker.producer.EventProducer
import com.kaique.application.web.config.Roles
import io.javalin.apibuilder.ApiBuilder.post
import io.javalin.http.Context

fun eventRoutes(eventProducer: EventProducer) {

    post("/", { ctx ->
        when {
            ctx.getContentTypeWithoutCharset() == "application/vnd.event_v1+json" -> {
                eventProducer.emit(
                    ctx.body<Event<String>>(),
                    ctx.header("correlationId")
                )

                producerEventResponse(ctx)
            }
            else -> ctx.notAcceptable()
        }
    }, setOf(Roles.SERVICE))
}

fun producerEventResponse(ctx: Context) {
    ctx.status(200)
    ctx.contentType("application/json")
}

fun Context.notAcceptable() {
    status(406)
    json("Invalid value for Content-Type header")
    contentType("application/json")
}

fun Context.getContentTypeWithoutCharset(): String? = this.contentType()?.split(";")?.first()