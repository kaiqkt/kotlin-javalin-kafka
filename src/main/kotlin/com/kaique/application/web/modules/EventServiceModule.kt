package com.kaique.application.web.modules

import com.kaique.domain.services.ListenerEventService
import org.koin.dsl.module.module

val eventServiceModule = module {
    single { ListenerEventService() }
}