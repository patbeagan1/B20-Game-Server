package com.pbeagan.domain.ancestry.type

import com.pbeagan.domain.actions.Pass
import com.pbeagan.stats.AttackValue
import com.pbeagan.stats.HealthValue
import com.pbeagan.stats.MentalValue
import com.pbeagan.stats.MiscValue
import com.pbeagan.domain.Mob
import com.pbeagan.domain.Mob.*
import com.pbeagan.stats.PhysicalValue
import com.pbeagan.stats.Stats
import com.pbeagan.stats.VisionValue

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

        override var totalHearts: HealthValue = HealthValue(4)

        override val visionBright: VisionValue = VisionValue(100)
        override val visionDim: VisionValue = VisionValue(7)
        override val visionDark: VisionValue = VisionValue(3)
        override val visionNone: VisionValue = VisionValue(1)
    }
) {
    override fun decide(mob: Mob, behavior: MobBehavior) = Pass
}