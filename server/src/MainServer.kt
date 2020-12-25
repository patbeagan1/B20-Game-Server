import com.pbeagan.Injection
import com.pbeagan.site.Router
import com.pbeagan.site.routers.DemoRouter
import com.pbeagan.site.routers.ReadDataRouter
import com.pbeagan.site.routers.UsernameRouter
import com.pbeagan.util.loggerGen
import com.pbeagan.writer.TerminalColorStyle
import com.pbeagan.writer.TerminalColorStyle.RIS
import com.pbeagan.writer.TerminalColorStyle.style
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.application.log
import io.ktor.features.CORS
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.gson.gson
import io.ktor.http.HttpHeaders
import io.ktor.http.content.default
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.response.respondBytes
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.koin.ktor.ext.Koin
import java.text.DateFormat

val debug = true

fun main(args: Array<String>) {
    embeddedServer(
        Netty,
        8082,
        host = "127.0.0.1",
        watchPaths = listOf("BlogAppKt"),
        module = Application::module
    ).start()
}

fun Application.module() {
    if (debug == true) ({ loadDebug() })() else null
    performSetup()
}

private fun Application.loadDebug() {
    install(CORS) {
        anyHost()
        header(HttpHeaders.AccessControlAllowOrigin)
    }
    log.warn("From App")
    val logger by lazy { loggerGen(this::class.java) }
    logger.run {
        info("test")
        warn("test")
        error("test")
    }
}

private fun Application.performSetup() {
    install(DefaultHeaders)
    install(CallLogging)
    install(ContentNegotiation) {
        gson {
            setDateFormat(DateFormat.LONG)
            setPrettyPrinting()
        }
    }
    install(Koin) { modules(Injection.helloAppModule) }
    install(Routing) {
        val logger by lazy { loggerGen(this::class.java) }
        static("/") {
            resources("client/build/")
            default("resources/client/build/index.html")
        }
        ReadDataRouter().executeWith(this)
        UsernameRouter(logger).executeWith(this)
        DemoRouter().executeWith(this)
        object : Router() {
            override fun get(): Routing.() -> Unit = {
                get("/{string}") {
                    val string = call.parameters["string"] + "\n"
                    call.respondBytes(
                        string.style(TerminalColorStyle.Colors.Red, TerminalColorStyle.Colors.White).toByteArray()
                    )
                }
            }
        }.executeWith(this)
    }
}