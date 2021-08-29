package com.kaique.application.web.modules

import com.fasterxml.jackson.databind.ObjectMapper
import com.kaique.resources.kafka.EventProducer
import org.koin.dsl.module.module

val eventProducerModule = module {
    single {
        EventProducer(
            getProperty("KAFKA_URL"),
            getProperty("KAFKA_TOPIC"),
            ObjectMapper()
        )
    }
}