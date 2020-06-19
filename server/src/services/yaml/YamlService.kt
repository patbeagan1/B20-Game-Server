package services.yaml

import services.yaml.data.Spell

interface YamlService {
    fun getSpells(): List<Spell>?
    fun getConversation(): List<Conversation>
}
