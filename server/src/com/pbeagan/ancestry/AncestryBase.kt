package com.pbeagan.ancestry

import com.pbeagan.actions.Pass
import com.pbeagan.data.CombatValue
import com.pbeagan.data.HealthValue
import com.pbeagan.data.MentalValue
import com.pbeagan.data.MiscValue
import com.pbeagan.data.Mob
import com.pbeagan.data.MobBehavior
import com.pbeagan.data.PhysicalValue
import com.pbeagan.data.Stats
import com.pbeagan.data.VisionValue

class AncestryBase : Ancestry(
    object : Stats {
        override var baseAtkMelee: CombatValue = CombatValue(0)
        override var baseAtkRanged: CombatValue = CombatValue(0)
        override var baseAtkThrow: CombatValue = CombatValue(0)

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