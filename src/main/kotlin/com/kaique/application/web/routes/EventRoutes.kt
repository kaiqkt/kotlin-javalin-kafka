package com.kaique.application.web.routes

import com.kaique.application.configs.Roles
import com.kaique.domain.entities.Event
import io.javalin.apibuilder.ApiBuilder.post
import io.javalin.http.BadRequestResponse
import io.javalin.http.Context

typealias EventHandler<T> = (event: Event<T>) -> Unit

inline fun <reified T: Any> eventRoute(noinline eventHandler: EventHandler<T>) = eventRoute(eventHandler, T::class.java)

fun <T> eventRoute(eventHandler: EventHandler<T>, clazz: Class<T>) {
    post("/", { ctx ->
        when {
            ctx.getContentTypeWithoutCharset() == "application/vnd.event_v1+json" -> {
                eventHandler(getEvent(ctx, clazz))

                producerEventResponse(ctx)
            }
            else -> ctx.notAcceptable()
        }
    }, setOf(Roles.SERVICE))
}

fun <T> getEvent(ctx: Context, clazz: Class<T>): Event<T> {
    return Event(
        correlationId = ctx.header("correlationId") ?: throw BadRequestResponse(),
        payload = ctx.bodyAsClass(clazz)
    )
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