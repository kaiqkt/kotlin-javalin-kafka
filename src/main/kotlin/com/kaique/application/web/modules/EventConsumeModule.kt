package com.kaique.application.web.modules

import com.fasterxml.jackson.databind.ObjectMapper
import com.kaique.resources.kafka.EventConsumer
import org.koin.dsl.module.module

val eventConsumerModule = module {
    single {
        EventConsumer(
            getProperty("KAFKA_URL"),
            getProperty("KAFKA_TOPIC"),
            ObjectMapper()
        )
    }
}