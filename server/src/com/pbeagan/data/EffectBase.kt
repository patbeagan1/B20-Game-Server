package com.pbeagan.data

abstract class EffectImpl(effect: Effect): Effect by effect

data class EffectBase(
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
