package com.kaique.application.web.modules

import com.kaique.domain.services.EmitEventService
import org.koin.dsl.module.module

val emitEventServiceModule = module {
    single {
        EmitEventService(get())
    }
}