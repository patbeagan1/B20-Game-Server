package site

import io.ktor.routing.Routing

abstract class Router {
    abstract fun get() : Routing.() -> Unit
    fun executeWith(routing: Routing) = get()(routing)
}