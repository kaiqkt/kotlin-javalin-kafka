package com.kaique.domain.entities

import com.fasterxml.jackson.annotation.JsonProperty

data class Event<T>(
    @JsonProperty("payload")
    val payload: T
)
