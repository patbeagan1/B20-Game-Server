package com.pbeagan.contextual.ancestry.type

import dev.patbeagan.b20.contextual.Mob
import dev.patbeagan.b20.contextual.Mob.MobBehavior
import com.pbeagan.contextual.actions.Pass
import dev.patbeagan.b20.WorldState
import dev.patbeagan.b20.domain.stats.AttackValue
import dev.patbeagan.b20.domain.stats.HealthValue
import dev.patbeagan.b20.domain.stats.MentalValue
import dev.patbeagan.b20.domain.stats.MiscValue
import dev.patbeagan.b20.domain.stats.PhysicalValue
import dev.patbeagan.b20.domain.stats.Stats
import dev.patbeagan.b20.domain.stats.VisionValue

class AncestryBase : Ancestry(
    object : Stats {
        override var baseAtkMelee: AttackValue = AttackValue(0)
        override var baseAtkRanged: AttackValue = AttackValue(0)
        override var baseAtkThrow: AttackValue = AttackValue(0)

        override var awareness: MiscValue = MiscValue(4)
        override var spirit: MiscValue = MiscValue(0)
        override var speed: MiscValue = MiscValue(4)

        override var presence: MentalValue = MentalValue(10)
        override var cunning: MentalValue = MentalValue(10)
        override var persuasion: MentalValue = MentalValue(10)
        override var tenacity: MentalValue = MentalValue(10)
        override var fortitude: MentalValue = MentalValue(10)

        override var strength: PhysicalValue = PhysicalValue(10)
        override var agility: PhysicalValue = PhysicalValue(10)
        override var precision: PhysicalValue = PhysicalValue(10)
        override var endurance: PhysicalValue = PhysicalValue(10)
        override var durability: PhysicalValue = PhysicalValue(10)

        override var totalHearts: HealthValue = HealthValue(50)

        override val visionBright: VisionValue = VisionValue(100)
        override val visionDim: VisionValue = VisionValue(7)
        override val visionDark: VisionValue = VisionValue(3)
        override val visionNone: VisionValue = VisionValue(1)
    }
) {
    override fun decide(mob: Mob, behavior: MobBehavior, worldState: WorldState) = Pass
}