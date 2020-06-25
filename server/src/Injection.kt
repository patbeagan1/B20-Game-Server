import com.pbeagan.services.hello.HelloRepository
import com.pbeagan.services.hello.HelloService
import com.pbeagan.services.hello.HelloServiceImpl
import controllers.MainController
import org.koin.dsl.module
import services.counter.CounterService
import services.counter.CounterServiceImpl
import services.yaml.YamlParser
import services.yaml.YamlService
import services.yaml.YamlServiceImpl
import util.loggerGen

object Injection {

    // Todo: Remove this once we can inject anywhere with `by inject`.
    fun provideYamlParser(): YamlParser = YamlParser()

    val helloAppModule = module {
        single<HelloService> { HelloServiceImpl(get()) } // get() Will resolve services.hello.HelloRepository
        factory { HelloRepository() }
        single<CounterService> { CounterServiceImpl() }
        single<YamlService> { YamlServiceImpl() }
        factory { loggerGen(this::class.java) }
        factory { MainController() }
    }
}