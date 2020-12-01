package site.routers

import io.ktor.routing.Routing
import io.ktor.routing.get
import org.koin.ktor.ext.inject
import site.Router
import site.controllers.CounterCommand
import site.controllers.CounterService

class DemoRouter : Router() {
    override fun get(): Routing.() -> Unit = {
        val serviceCounter: CounterService by inject()

        get("/demo/counter", CounterCommand(serviceCounter).execute())
    }
}