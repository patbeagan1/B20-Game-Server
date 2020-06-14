import io.ktor.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main(args: Array<String>) {
    embeddedServer(
        Netty,
        8080,
        host = "127.0.0.1",
        watchPaths = listOf("BlogAppKt"),
        module = Application::module
    ).start()
}