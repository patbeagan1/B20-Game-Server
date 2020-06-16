package services.counter

class CounterServiceImpl : CounterService {
    private var counter: Int = 0
        get() {
            val ret = field
            field++
            return ret
        }
    override fun increment(): String = counter.toString()
}