package com.pbeagan.ancestry

import com.pbeagan.actions.Pass
import com.pbeagan.data.Mob
import com.pbeagan.data.MobBehavior
import com.pbeagan.data.Stats

class AncestryBase : Ancestry(
    object : Stats {
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

        override val visionBright: Int = 100
        override val visionDim: Int = 7
        override val visionDark: Int = 3
        override val visionNone: Int = 1
    }
) {
    override fun decide(mob: Mob, behavior: MobBehavior) = Pass
}