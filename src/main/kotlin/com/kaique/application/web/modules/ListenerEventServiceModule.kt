package com.kaique.application.web.modules

import com.kaique.domain.services.ListenerEventService
import org.koin.dsl.module.module

val listenerEventServiceModule = module {
    single { ListenerEventService() }
}