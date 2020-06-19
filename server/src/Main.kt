import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.application.log
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

    // comment this next line out if not testing
    enableCors()

    install(DefaultHeaders)
    install(CallLogging)
    install(ContentNegotiation) {
        gson {
            setDateFormat(DateFormat.LONG)
            setPrettyPrinting()
        }
    }
    install(Koin) { modules(Injection.helloAppModule) }
    install(Routing) { routes() }
    loggerExample()
}

private fun Application.loggerExample() {
    // apparently there is already a logger baked in
    this.log.warn("From App")
    // TODO: get this working with Koin parameter injection
    val logger by lazy { loggerGen(this::class.java) }
    logger.run {
        info("test")
        warn("test")
        error("test")
    }
}

private fun Application.enableCors() {
    install(CORS) {
        anyHost()
        header(HttpHeaders.AccessControlAllowOrigin)
    }
}
