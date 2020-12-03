package com.pbeagan.site.controllers

import io.ktor.application.call
import io.ktor.response.respondText
import com.pbeagan.site.Command
import com.pbeagan.site.Pipeline

class CounterCommand(private val service: CounterService) : Command() {
    override fun execute(): Pipeline = {
        call.respondText(service.increment())
    }
}

class CounterServiceImpl : CounterService {
    private var counter: Int = 0
        get() {
            val ret = field
            field++
            return ret
        }

    override fun increment(): String = counter.toString()
}

interface CounterService {
    fun increment(): String
}
