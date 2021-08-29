package com.kaique.application.web

import com.kaique.application.web.broker.consumer.EventConsumer
import com.kaique.application.web.broker.entities.Event
import com.kaique.application.web.broker.producer.EventProducer
import com.kaique.application.web.config.AuthConfig
import com.kaique.application.web.modules.authenticationModule
import com.kaique.application.web.modules.eventConsumerModule
import com.kaique.application.web.modules.eventProducerModule
import com.kaique.application.web.modules.eventServiceModule
import com.kaique.application.web.routes.eventRoutes
import com.kaique.domain.services.EventService
import io.javalin.Javalin
import org.koin.log.EmptyLogger
import org.koin.standalone.KoinComponent
import org.koin.standalone.StandAloneContext
import org.koin.standalone.inject
import org.koin.standalone.property

object EventEntryPoint : KoinComponent {

    private val eventConsumer: EventConsumer by inject()
    private val eventProducer: EventProducer by inject()
    private val service: EventService by inject()
    private val authConfig: AuthConfig by inject()
    private val serverPort: Int by property("SERVER_PORT")

    fun init(extraProperties: Map<String, Any> = emptyMap()) {
        StandAloneContext.startKoin(
            listOf(
                eventConsumerModule,
                eventProducerModule,
                eventServiceModule,
                authenticationModule
            ),
            useEnvironmentProperties = true,
            extraProperties = extraProperties,
            logger = EmptyLogger()
        )

        Javalin.create().apply {
            authConfig.configure(this)
            routes { eventRoutes(eventProducer) }
            start(serverPort)
        }

        eventConsumer.onMessage(service::listener)
    }
}

