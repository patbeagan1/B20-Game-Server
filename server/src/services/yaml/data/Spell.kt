package services.yaml.data

data class Spell(
    val name: String?,
    val mana: String?,
    val type: String?,
    val time: String?,
    val unlearnedPenalty: Int?,
    val learned: String?,
    val alwaysAvailable: String?,
    val description: String?
)
