package controllers

import com.pbeagan.models.data.Player
import com.pbeagan.models.data.WorldState
import com.pbeagan.services.hello.HelloService
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.util.pipeline.PipelineContext
import services.counter.CounterService
import services.yaml.YamlService

class MainController {

    var model: WorldState = WorldState(
        listOf(Player.random(), Player.random(), Player.random()),
        listOf(),
        listOf(),
        listOf()
    )

    fun hello(service: HelloService): suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit = {
        call.respondText(service.sayHello())
    }

    fun counter(service: CounterService): suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit = {
        call.respondText(service.increment())
    }

    fun tryYaml(service: YamlService): suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit = {
        call.respondText(model.players.joinToString(separator = ",") { it.show() })
        service.getConversation()
    }

    fun user(
        message: List<Pair<String, String>>,
        name: String?
    ): suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit = {
        call.respondText("n: $name -- m: $message")
    }

    fun root(): suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit = {
        call.respond(model)
    }
}

