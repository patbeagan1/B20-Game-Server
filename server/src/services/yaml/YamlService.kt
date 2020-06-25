package services.yaml

import com.pbeagan.models.Conversation
import services.yaml.data.Spell

interface YamlService {
    fun getSpells(): List<Spell>?
    fun getConversation(): List<Conversation>
    fun getDiscussion(): Any?
}
