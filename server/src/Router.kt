import controllers.MainController
import io.ktor.application.call
import io.ktor.http.Parameters
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
import util.loggerGen

fun Routing.routes() {
    val controller by inject<MainController>()
    val logger by lazy { loggerGen(this::class.java) }
    static("/") {
        resources("client/build/")
        default("resources/client/build/index.html")
    }

    get("/worldstate", controller.root())
    get("/user/{username}/") {
        val request = call.request
//        val queryParameters: Parameters = request.queryParameters
        val param1: String? = request.queryParameters["param1"] // To access a single parameter (first one if repeated)
//        val repeatedParam: List<String>? = request.queryParameters.getAll("repeatedParam") // Multiple values

        logger.warn("$param1")
        controller.user(
            request.queryParameters.flattenEntries().also { println(it) },
            call.parameters["username"]
        )
    }
    setupDemoRoutes(controller)
}

private fun Routing.setupDemoRoutes(controller: MainController) {
    val service: HelloService by inject()
    val serviceYaml: YamlService by inject()
    val counterService: CounterService by inject()

    get("/demo/yaml", controller.tryYaml(serviceYaml))
    get("/demo/counter", controller.counter(counterService))
    get("/demo/hello", controller.hello(service))
}

