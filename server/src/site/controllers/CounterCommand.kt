package site.controllers

import site.Command
import site.Pipeline
import io.ktor.application.call
import io.ktor.response.respondText

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
