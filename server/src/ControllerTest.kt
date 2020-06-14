import data.WorldState

internal class ControllerTest {

    private val controller = Controller(
        WorldState(
            emptyList(),
            emptyList(),
            emptyList(),
            emptyList()
        ),
        View()
    )

    @Test
    fun tryYaml() {
        controller.tryYaml()
    }

    @Test
    fun getCounter() {
    }
}