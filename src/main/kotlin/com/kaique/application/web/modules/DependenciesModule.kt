package com.kaique.application.web.modules

import com.fasterxml.jackson.databind.ObjectMapper
import com.kaique.application.configs.AuthConfig
import com.kaique.application.configs.mongoDatabase
import com.kaique.resources.kafka.EventConsumer
import com.kaique.resources.kafka.EventProducer
import com.mongodb.ConnectionString
import org.koin.dsl.module.module

val dependenciesModule = module {
    single {
        AuthConfig(getProperty("SERVICE_TOKEN"))
    }

    single {
        mongoDatabase(
            connectionString = ConnectionString("mongodb://${getProperty<String>("MONGODB_CONNECTION_STRING")}"),
            username = getProperty<String>("MONGODB_USER"),
            password = getProperty<String>("MONGODB_PASSWORD"),
            databaseName = getProperty<String>("MONGODB_DATABASE")
        )
    }

    single {
        EventConsumer(
            getProperty("KAFKA_URL"),
            getProperty("KAFKA_TOPIC"),
            ObjectMapper()
        )
    }

    single {
        EventProducer(
            getProperty("KAFKA_URL"),
            getProperty("KAFKA_TOPIC"),
            ObjectMapper()
        )
    }
}