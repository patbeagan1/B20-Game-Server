package com.pbeagan.domain.ancestry

import com.pbeagan.domain.actions.type.Action
import com.pbeagan.domain.actions.Drop
import com.pbeagan.domain.actions.Move
import com.pbeagan.domain.actions.Pass
import com.pbeagan.domain.actions.Take
import com.pbeagan.domain.ancestry.type.Ancestry
import com.pbeagan.domain.ancestry.type.AncestryBase
import com.pbeagan.domain.attacks.Punch
import com.pbeagan.domain.types.Direction
import com.pbeagan.stats.MentalValue
import com.pbeagan.domain.Mob
import com.pbeagan.domain.Mob.MobBehavior
import com.pbeagan.domain.getFirstVisibleMob
import com.pbeagan.domain.getRandomVisibleItem
import com.pbeagan.util.roll20

class Goblin : Ancestry(AncestryBase()) {

    override val fortitude: MentalValue get() = super.fortitude - MentalValue(3)

    override fun decide(mob: Mob, behavior: MobBehavior): Action {
        return mob.run {
            when (behavior) {
                MobBehavior.LOOTER -> {
                    when (roll20().value) {
                        in 0..2 -> getRandomVisibleItem()?.let { Take(it) }
                        in 3..4 -> this.items.takeIf { it.isNotEmpty() }?.random()?.let { Drop(it) }
                        else -> Pass
                    } ?: Pass
                }
                MobBehavior.AGGRESSIVE -> {
                    when (roll20().value) {
                        in 0..18 -> getFirstVisibleMob()?.let { Punch(it) }
                        else -> Pass
                    } ?: Pass
                }
                MobBehavior.WANDERER -> when (roll20().value) {
                    0 -> Move.forceMove(Direction.NORTH)
                    1 -> Move.forceMove(Direction.EAST)
                    2 -> Move.forceMove(Direction.SOUTH)
                    3 -> Move.forceMove(Direction.WEST)
                    else -> Pass
                }
                else -> TODO()
            }
        }
    }
}