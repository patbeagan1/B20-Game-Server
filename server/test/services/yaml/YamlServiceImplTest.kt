package services.yaml

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class YamlServiceImplTest {

    lateinit var yamlServiceImpl: YamlService

    @BeforeEach
    fun setup() {
        yamlServiceImpl = YamlServiceImpl()
    }

    @Test
    fun getSpells() {
        val spells = yamlServiceImpl.getSpells()
        println(spells)
        assertTrue(spells?.size ?: 0 > 1)
    }

    @Test
    fun getConversation() {
        val spells = yamlServiceImpl.getConversation()
        println(spells)
    }

    @Test
    fun getDiscussion() {
        val spells = yamlServiceImpl.getDiscussion()
        println(spells)
    }
}