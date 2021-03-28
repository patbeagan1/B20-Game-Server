package com.pbeagan.ancestry

import com.pbeagan.actions.Action
import com.pbeagan.actions.Drop
import com.pbeagan.actions.Move
import com.pbeagan.actions.Pass
import com.pbeagan.actions.Take
import com.pbeagan.actions.attacks.Punch
import com.pbeagan.data.Direction
import com.pbeagan.data.MentalValue
import com.pbeagan.data.Mob
import com.pbeagan.data.MobBehavior
import com.pbeagan.data.getFirstVisibleMob
import com.pbeagan.data.getRandomVisibleItem
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