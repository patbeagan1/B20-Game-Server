package services.yaml

import Injection
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import data.Player
import data.WorldState
import org.yaml.snakeyaml.Yaml
import services.yaml.data.Spell
import java.nio.file.Files
import java.nio.file.Paths


class YamlServiceImpl : YamlService {
    val parser = Injection.provideYamlParser()

    override fun getSpells(): List<Spell>? = parser.parseYamlList("assets/spells.yaml") {
        println(it)
        Spell(
            "name" from it,
            "mana" from it,
            "type" from it,
            "time" from it,
            ("unlearnedPenalty" from it)?.toInt(),
            "learned" from it,
            "alwaysAvailable" from it,
            "description" from it
        )
    }

    override fun getConversation() = parser.parseYamlList("assets/conversation1.yaml") { top ->
        getConversation2()

        Conversation(
            ("id" from top)!!,
            ("next" from top)!!,
            "firstsay" from top,
            "say" from top,
            top["options"].let { it as? ArrayList<HashMap<String, Any>> }?.map {
                Conversation.Option(
                    ("id" from it)!!,
                    ("next" from it),
                    "say" from it
                )
            }
        )
    }

    fun getConversation2() {
        val loadAs = Yaml()
            .loadAs(
                Files.newBufferedReader(
                    Paths.get("assets/conversation1.yaml")
                ), ConversationList::class.java
            )
        println(loadAs)
    }

    override fun getDiscussion() {
        val mapper = XmlMapper.xmlBuilder()
            .enable(SerializationFeature.INDENT_OUTPUT)
            .defaultUseWrapper(false)
            .build()

        val value: Simple = mapper.readValue(
            """
<Simple>
<x>1</x>
<y>2</y>
</Simple>""".trimIndent(), Simple::class.java
        )

        mapper.writeValueAsString(listOf("hello", "2")).also { println(it) }
        mapper.writeValueAsString(
            WorldState(
                listOf(Player.random(), Player.random(), Player.random()),
                listOf(),
                listOf(),
                listOf()
            )
        ).also { println(it) }


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

        // https://proandroiddev.com/parsing-optional-values-with-jackson-and-kotlin-36f6f63868ef
        val omapper = XmlMapper().registerKotlinModule()

        val value2 = omapper.readValue(
            Files.newBufferedReader(
                Paths.get("assets/conversation.xml")
            ), ConversationList::class.java
        )

        println(value)
        println(value2)

    }

    private infix fun String.from(map: HashMap<String, Any>): String? = map[this]?.toString()
}

data class Simple(
    var x: Int = 3,
    var y: Int = 4
)


@JacksonXmlRootElement(localName = "discussion")
data class ConversationList(
    @JacksonXmlElementWrapper(useWrapping = false)
    val conversation: List<Conversation>
)

data class Conversation constructor(
    @JacksonXmlProperty(isAttribute = true) val id: String,
    @JacksonXmlProperty(isAttribute = true) val next: String?,
    val firstsay: String?,
    val say: String?,
    @JacksonXmlElementWrapper(useWrapping = false) val option: List<Option>?
) {
    data class Option(
        @JacksonXmlProperty(isAttribute = true) val id: String,
        @JacksonXmlProperty(isAttribute = true) val next: String?,
        val say: String?
    )
}