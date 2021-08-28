package com.kaique.application

import com.kaique.application.web.EventEntryPoint


object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        EventEntryPoint.init()
    }
}