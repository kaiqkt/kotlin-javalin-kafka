package com.kaique.application.web.modules

import com.kaique.application.web.config.AuthConfig
import org.koin.dsl.module.module

val authenticationModule = module {
    single { AuthConfig(getProperty("SERVICE_TOKEN")) }
}