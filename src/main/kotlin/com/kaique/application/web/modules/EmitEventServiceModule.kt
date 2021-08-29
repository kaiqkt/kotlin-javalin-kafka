package com.kaique.application.web.modules

import com.kaique.domain.gateways.EventRepository
import com.kaique.domain.services.EmitEventService
import com.kaique.resources.repositories.EventMongoRepository
import org.koin.dsl.module.module

val emitEventServiceModule = module {
    single {
        EmitEventService(get())
    }
    single<EventRepository> {
        EventMongoRepository(
            mongo = get(),
            collectionName = getProperty("MONGODB_EVENT_COLLECTION")
        )
    }
}