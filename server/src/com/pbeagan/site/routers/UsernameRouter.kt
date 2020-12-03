package com.pbeagan.site.routers

import io.ktor.application.call
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.util.flattenEntries
import org.slf4j.Logger
import com.pbeagan.site.Router
import com.pbeagan.site.controllers.UserCommand

class UsernameRouter(val logger: Logger) : Router() {
    override fun get() = fun Routing.() {

        get("/user/{username}/") {
            val request = call.request
//        val queryParameters: Parameters = request.queryParameters
            val param1: String? = request.queryParameters["param1"] // To access a single parameter (first one if repeated)
//        val repeatedParam: List<String>? = request.queryParameters.getAll("repeatedParam") // Multiple values

            logger.warn("$param1")
            UserCommand(
                request.queryParameters.flattenEntries().also { println(it) },
                call.parameters["username"]
            ).execute()
        }
    }
}
