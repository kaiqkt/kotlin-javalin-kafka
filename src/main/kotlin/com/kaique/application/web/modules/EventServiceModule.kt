package com.kaique.application.web.modules

import com.kaique.domain.services.EventService
import org.koin.dsl.module.module

val eventServiceModule = module {
    single { EventService() }
}