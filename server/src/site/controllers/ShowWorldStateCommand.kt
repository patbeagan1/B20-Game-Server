package site.controllers

import site.Command
import site.Pipeline
import com.pbeagan.models.worldstate.WorldState
import io.ktor.application.call
import io.ktor.response.respond

class ShowWorldStateCommand(private val model: WorldState) : Command() {
    override fun execute(): Pipeline {
        return { call.respond(model) }
    }
}