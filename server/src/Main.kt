import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.CORS
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.gson.gson
import io.ktor.http.HttpHeaders
import io.ktor.routing.Routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.koin.ktor.ext.Koin
import util.loggerGen
import java.text.DateFormat

fun main(args: Array<String>) {
    embeddedServer(
        Netty,
        8080,
        host = "127.0.0.1",
        watchPaths = listOf("BlogAppKt"),
        module = Application::module
    ).start()
}

fun Application.module() {
    install(DefaultHeaders)
    install(CallLogging)
    install(CORS) {
        anyHost()
        header(HttpHeaders.AccessControlAllowOrigin)
        allowCredentials = true
    }
    install(ContentNegotiation) {
        gson {
            setDateFormat(DateFormat.LONG)
            setPrettyPrinting()
        }
    }
    install(Koin) {
        val logger = loggerGen()
        logger.info("Test")
        modules(Injection.helloAppModule)
    }
    install(Routing) { routes() }
}
