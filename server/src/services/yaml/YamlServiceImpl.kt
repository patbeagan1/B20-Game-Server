package services.yaml

import Injection
import com.pbeagan.models.Conversation
import com.pbeagan.models.ConversationList
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



    }

    private infix fun String.from(map: HashMap<String, Any>): String? = map[this]?.toString()
}



