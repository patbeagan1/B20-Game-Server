package com.pbeagan.data

import com.pbeagan.data.Effect.Type.ANCESTRY

data class EffectImpl(
    // Expiry - the only var
    // This stat block will be removed when the expiry reaches 0
    override val name: String,
    override var roundsLeft: Int,
    override val type: Effect.Type,

    override val descriptionActivation: String,
    override val descriptionDeactivation: String,

    // Combat
    override var baseAtkMelee: Int = 0,
    override var baseAtkRanged: Int = 0,
    override var baseAtkThrow: Int = 0,

    // Misc
    override var awareness: Int = 0,
    override var spirit: Int = 0,
    override var speed: Int = 0,

    // Mental
    override var presence: Int = 0,
    override var cunning: Int = 0,
    override var persuasion: Int = 0,
    override var tenacity: Int = 0,
    override var fortitude: Int = 0,

    // Physical
    override var strength: Int = 0,
    override var agility: Int = 0,
    override var precision: Int = 0,
    override var endurance: Int = 0,
    override var durability: Int = 0,

    // Total hearts
    override var totalHearts: Int = 0
) : Effect


abstract class Ancestry(val stats: Stats) : Effect, Stats by stats {
    override var roundsLeft: Int = Int.MAX_VALUE
    override var type: Effect.Type = ANCESTRY
    override var name: String = "${this::class.java.simpleName} ancestry"
    override var descriptionActivation: String = ""
    override var descriptionDeactivation: String = ""
}

class BaseAncestry : Ancestry(object : Stats {
    override var baseAtkMelee: Int = 0
    override var baseAtkRanged: Int = 0
    override var baseAtkThrow: Int = 0
    override var awareness: Int = 4
    override var spirit: Int = 0
    override var speed: Int = 4
    override var presence: Int = 10
    override var cunning: Int = 10
    override var persuasion: Int = 10
    override var tenacity: Int = 10
    override var fortitude: Int = 10
    override var strength: Int = 10
    override var agility: Int = 10
    override var precision: Int = 10
    override var endurance: Int = 10
    override var durability: Int = 10
    override var totalHearts: Int = 4
})

class Human : Ancestry(BaseAncestry())

class Goblin : Ancestry(BaseAncestry()) {
    override val fortitude: Int get() = super.fortitude - 3
}

class Hobgoblin : Ancestry(Goblin()) {
    override val fortitude: Int get() = super.fortitude - 4
}

interface Effect : Stats {
    var roundsLeft: Int
    val name: String
    val descriptionActivation: String
    val descriptionDeactivation: String
    val type: Type

    enum class Type {
        ANCESTRY, SPELL
    }
}