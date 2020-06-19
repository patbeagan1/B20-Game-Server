package services.hello

class HelloServiceImpl(
    private val helloRepository: HelloRepository
) : HelloService {

    override fun sayHello() =
        "Hello ${helloRepository.getHello()} !"

    override fun tryJson() {
        helloRepository
            .getDemoJson()
            ?.forEach { println(it) }
    }
}