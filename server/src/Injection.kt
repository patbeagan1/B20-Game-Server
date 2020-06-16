import org.koin.dsl.module
import services.counter.CounterService
import services.counter.CounterServiceImpl
import services.hello.HelloRepository
import services.hello.HelloService
import services.hello.HelloServiceImpl
import services.yaml.YamlService
import services.yaml.YamlServiceImpl

object Injection {
    val helloAppModule = module {
        single<HelloService> { HelloServiceImpl(get()) } // get() Will resolve services.hello.HelloRepository
        single { HelloRepository() }
        single<CounterService> { CounterServiceImpl() }
        single<YamlService> { YamlServiceImpl() }
    }
}