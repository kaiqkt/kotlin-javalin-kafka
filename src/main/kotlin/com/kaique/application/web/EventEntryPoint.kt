package com.kaique.application.web

import com.kaique.application.configs.AuthConfig
import com.kaique.application.web.modules.*
import com.kaique.application.web.routes.eventRoutes
import com.kaique.domain.services.EmitEventService
import com.kaique.domain.services.ListenerEventService
import com.kaique.resources.kafka.EventConsumer
import io.javalin.Javalin
import org.koin.log.EmptyLogger
import org.koin.standalone.KoinComponent
import org.koin.standalone.StandAloneContext
import org.koin.standalone.inject
import org.koin.standalone.property

object EventEntryPoint : KoinComponent {

    private val eventConsumer: EventConsumer by inject()
    private val emitEventService: EmitEventService by inject()
    private val listenerEventService: ListenerEventService by inject()
    private val authConfig: AuthConfig by inject()
    private val serverPort: Int by property("SERVER_PORT")

    fun init(extraProperties: Map<String, Any> = emptyMap()) {
        StandAloneContext.startKoin(
            listOf(
                dependenciesModule,
                emitEventServiceModule,
                listenerEventServiceModule
            ),
            useEnvironmentProperties = true,
            extraProperties = extraProperties,
            logger = EmptyLogger()
        )

        Javalin.create().apply {
            authConfig.configure(this)
            routes { eventRoutes(emitEventService) }
            start(serverPort)
        }

        eventConsumer.onMessage(listenerEventService::listener)
    }
}

