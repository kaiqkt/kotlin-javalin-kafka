package com.kaique.application.web.routes

import com.kaique.domain.entities.Event
import com.kaique.application.configs.Roles
import com.kaique.domain.services.EmitEventService
import io.javalin.apibuilder.ApiBuilder.post
import io.javalin.http.Context

fun eventRoutes(emitEventService: EmitEventService) {

    post("/", { ctx ->
        when {
            ctx.getContentTypeWithoutCharset() == "application/vnd.event_v1+json" -> {
                emitEventService.send(
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