package com.pbeagan.site.routers

import io.ktor.routing.Routing
import io.ktor.routing.get
import org.koin.ktor.ext.inject
import com.pbeagan.site.Router
import com.pbeagan.site.controllers.CounterCommand
import com.pbeagan.site.controllers.CounterService

class DemoRouter : Router() {
    override fun get(): Routing.() -> Unit = {
        val serviceCounter: CounterService by inject()

        get("/demo/counter", CounterCommand(serviceCounter).execute())
    }
}
