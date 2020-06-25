package services.yaml

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import data.Player
import data.WorldState
import org.junit.Test
import java.nio.file.Files
import java.nio.file.Paths

internal class XMLRepositoryTest {

    private var repository: XMLRepository = XMLRepository()
    var mapper: ObjectMapper = repository.mapper

    @Test
    fun `basic xml to object mapping works`() {
        mapper.readValue(
            simpleXML.trimIndent(),
            Simple::class.java
        )
    }

    @Test
    fun `simple java objects can be converted into xml`() {
        mapper.writeValueAsString(listOf("hello", "2")).also { println(it) }
    }

    @Test
    fun `conversations can be represented as XML`() {


        mapper.writeValueAsString(
            ConversationList(
                listOf(
                    Conversation(
                        "1",
                        "1", "Test first", "Say again", listOf(
                            Conversation.Option("1", "1", "Response")
                        )
                    ),
                    Conversation(
                        "1",
                        "1", "Test first second", "Say again", listOf(
                            Conversation.Option("1", "1", "Response")
                        )
                    )
                )
            )
        ).also { println(it) }
    }

    @Test
    fun `worldstate can be represented as XML`() {
        mapper.writeValueAsString(
            WorldState(
                listOf(Player.random(), Player.random(), Player.random()),
                listOf(),
                listOf(),
                listOf()
            )
        ).also { println(it) }
    }

    @Test
    fun `conversation_xml is parsable`() {
        // https://proandroiddev.com/parsing-optional-values-with-jackson-and-kotlin-36f6f63868ef
        val omapper = XmlMapper().registerKotlinModule()

        val value2 = omapper.readValue(
            Files.newBufferedReader(
                Paths.get("assets/conversation.xml")
            ), ConversationList::class.java
        )
    }

    data class Simple(
        var x: Int = 3,
        var y: Int = 4
    )

    companion object {
        const val simpleXML = """
<Simple>
<x>1</x>
<y>2</y>
</Simple>"""
    }
}