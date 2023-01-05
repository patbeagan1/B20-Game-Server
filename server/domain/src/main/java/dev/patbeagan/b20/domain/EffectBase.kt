package dev.patbeagan.b20.domain

import dev.patbeagan.b20.domain.stats.AttackValue
import dev.patbeagan.b20.domain.stats.HealthValue
import dev.patbeagan.b20.domain.stats.MentalValue
import dev.patbeagan.b20.domain.stats.MiscValue
import dev.patbeagan.b20.domain.stats.PhysicalValue
import dev.patbeagan.b20.domain.stats.VisionValue

abstract class EffectImpl(effect: Effect) : Effect by effect

data class EffectBase(
    // Expiry - the only var
    // This stat block will be removed when the expiry reaches 0
    override val name: String,
    override var roundsLeft: Int,
    override val type: Effect.Type,

    override val descriptionActivation: String,
    override val descriptionDeactivation: String,

    // Combat
    override var baseAtkMelee: AttackValue = AttackValue(0),
    override var baseAtkRanged: AttackValue = AttackValue(0),
    override var baseAtkThrow: AttackValue = AttackValue(0),

    // Misc
    override var awareness: MiscValue = MiscValue(0),
    override var spirit: MiscValue = MiscValue(0),
    override var speed: MiscValue = MiscValue(0),

    // Mental
    override var presence: MentalValue = MentalValue(0),
    override var cunning: MentalValue = MentalValue(0),
    override var persuasion: MentalValue = MentalValue(0),
    override var tenacity: MentalValue = MentalValue(0),
    override var fortitude: MentalValue = MentalValue(0),

    // Physical
    override var strength: PhysicalValue = PhysicalValue(0),
    override var agility: PhysicalValue = PhysicalValue(0),
    override var precision: PhysicalValue = PhysicalValue(0),
    override var endurance: PhysicalValue = PhysicalValue(0),
    override var durability: PhysicalValue = PhysicalValue(0),

    // Total hearts
    override var totalHearts: HealthValue = HealthValue(0),

    // vision
    override var visionBright: VisionValue = VisionValue(0),
    override var visionDim: VisionValue = VisionValue(0),
    override var visionDark: VisionValue = VisionValue(0),
    override var visionNone: VisionValue = VisionValue(0)
) : Effect
