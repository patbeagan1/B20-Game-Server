package com.pbeagan.contextual.ancestry

import com.pbeagan.contextual.Mob
import com.pbeagan.contextual.Mob.MobBehavior
import com.pbeagan.contextual.actions.Drop
import com.pbeagan.contextual.actions.Move
import com.pbeagan.contextual.actions.Pass
import com.pbeagan.contextual.actions.Take
import com.pbeagan.contextual.actions.type.Action
import com.pbeagan.contextual.ancestry.type.Ancestry
import com.pbeagan.contextual.attacks.Punch
import com.pbeagan.contextual.getFirstVisibleMob
import com.pbeagan.contextual.getRandomVisibleItem
import dev.patbeagan.b20.domain.roll20
import dev.patbeagan.b20.domain.stats.HealthValue
import dev.patbeagan.b20.domain.stats.MentalValue
import dev.patbeagan.b20.domain.types.Direction

class Hobgoblin : Ancestry(Goblin()) {

    override val fortitude: MentalValue get() = super.fortitude - MentalValue(4)
    override val totalHearts: HealthValue get() = super.totalHearts + HealthValue(5)

    override fun decide(mob: Mob, behavior: MobBehavior): Action = mob.run {
        when (behavior) {
            MobBehavior.LOOTER -> {
                when (roll20().value) {
                    in 0..2 -> getRandomVisibleItem()?.let { Take(it) }
                    in 3..4 -> this.items.takeIf { it.isNotEmpty() }?.random()?.let {
                        Drop(
                            it
                        )
                    }
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