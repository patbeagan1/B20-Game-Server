import data.Player
import data.WorldState
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.gson.gson
import io.ktor.http.ContentType
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.util.flattenEntries
import java.text.DateFormat

val controller = Controller(
    WorldState(
        listOf(
            Player.random(),
            Player.random(),
            Player.random()
        ), listOf(), listOf(), listOf()
    ),
    View()
)

fun Application.module() {
    install(DefaultHeaders)
    install(CallLogging)
    install(ContentNegotiation) {
        gson {
            setDateFormat(DateFormat.LONG)
            setPrettyPrinting()
        }
    }
    install(Routing) { routes() }
}


private fun Routing.routes() {
    get("/other") {
        call.respondText("My Example Blog2", ContentType.Text.Html)
    }
    get("/") {
        call.respond(controller.world)
    }
    get("/user/{username}/") {
        val message = call.request.queryParameters.flattenEntries()
        println(message)
        val name = call.parameters["username"]
        call.respondText("n: $name -- m: $message")
    }
    get("/demo") {
        call.respondText(controller.world.players.joinToString(separator = ",") { it.show() })
        controller.tryYaml()
    }
    get("/counter") {
        call.respondText(controller.counter.toString())
    }
}
