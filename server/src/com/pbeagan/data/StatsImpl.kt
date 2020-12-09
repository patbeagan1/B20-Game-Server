package com.pbeagan.data

data class StatsImpl(

    // Expiry - the only var
    // This stat block will be removed when the expiry reaches 0
    var roundsLeft: Int = 0,

    val descriptionActivation: String = "",
    val descriptionDeactivation: String = "",

    // Combat
    override val baseAtkMelee: Int = 0,
    override val baseAtkRanged: Int = 0,
    override val baseAtkThrow: Int = 0,

    // Misc
    override val awareness: Int = 0,
    override val spirit: Int = 0,
    override var speed: Int = 0,

    // Mental
    override val presence: Int = 0,
    override val cunning: Int = 0,
    override val persuasion: Int = 0,
    override val tenacity: Int = 0,
    override val fortitude: Int = 0,

    // Physical
    override val strength: Int = 0,
    override val agility: Int = 0,
    override val precision: Int = 0,
    override val endurance: Int = 0,
    override val durability: Int = 0,

    // Total hearts
    override val totalHearts: Int = 0

) : Stats {

    companion object {
        fun getInitial() = StatsImpl(
            roundsLeft = Int.MAX_VALUE,
            baseAtkMelee = 0,
            baseAtkRanged = 0,
            baseAtkThrow = 0,
            awareness = 4,
            spirit = 0,
            speed = 4,
            presence = 10,
            cunning = 10,
            persuasion = 10,
            tenacity = 10,
            fortitude = 10,
            strength = 10,
            agility = 10,
            precision = 10,
            endurance = 10,
            durability = 10,
            totalHearts = 4
        )
    }
}