import controllers.MainController
import io.ktor.application.call
import io.ktor.http.content.default
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.util.flattenEntries
import org.koin.ktor.ext.inject
import services.counter.CounterService
import services.hello.HelloService
import services.yaml.YamlService

fun Routing.routes() {
    val controller = MainController()

    val service: HelloService by inject()
    val serviceYaml: YamlService by inject()
    val counterService: CounterService by inject()

    get("/", controller.root())
    get("/user/{username}/") {
        controller.user(
            call.request.queryParameters.flattenEntries().also { println(it) },
            call.parameters["username"]
        )
    }
    get("/demo", controller.tryYaml(serviceYaml))
    get("/counter", controller.counter(counterService))
    get("/services/hello", controller.hello(service))

    static("static") {
        resources("client/build/")
        default("resources/client/build/index.html")
    }
}

