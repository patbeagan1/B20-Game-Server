import controllers.MainController
import io.ktor.application.call
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
}

