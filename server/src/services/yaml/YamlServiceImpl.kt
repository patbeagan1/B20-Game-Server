package services.yaml

import Injection
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
            ("id" from top)?.toInt(),
            "firstsay" from top,
            "say" from top,
            top["options"].let { it as? ArrayList<HashMap<String, Any>> }?.map {
                Conversation.Option(
                    ("id" from it)?.toInt(),
                    "say" from it,
                    ("next" from it)?.toInt()
                )
            },
            ("next" from top)?.toInt()
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

    private infix fun String.from(map: HashMap<String, Any>): String? = map[this]?.toString()
}

class ConversationList : ArrayList<Conversation>()
data class Conversation(
    val id: Int?,
    val firstsay: String?,
    val say: String?,
    val options: List<Option>?,
    val next: Int?
) {
    data class Option(
        val id: Int?,
        val say: String?,
        val next: Int?
    )
}