package com.pbeagan.contextual.ancestry

import dev.patbeagan.b20.contextual.Mob
import dev.patbeagan.b20.contextual.Mob.MobBehavior
import dev.patbeagan.b20.contextual.actions.AttackUnarmed
import com.pbeagan.contextual.actions.Drop
import dev.patbeagan.b20.contextual.actions.Move
import com.pbeagan.contextual.actions.Pass
import dev.patbeagan.b20.contextual.actions.Take
import com.pbeagan.contextual.actions.type.Action
import com.pbeagan.contextual.ancestry.type.Ancestry
import com.pbeagan.contextual.ancestry.type.AncestryBase
import dev.patbeagan.b20.WorldState
import dev.patbeagan.b20.domain.roll20
import dev.patbeagan.b20.domain.stats.MentalValue
import dev.patbeagan.b20.domain.types.Direction

class Goblin : Ancestry(AncestryBase()) {

    override val fortitude: MentalValue get() = super.fortitude - MentalValue(3)

    override fun decide(
        mob: Mob,
        behavior: MobBehavior,
        worldState: WorldState,
    ): Action = worldState.run {
        when (behavior) {
            MobBehavior.LOOTER -> {
                when (roll20().value) {
                    in 0..2 -> mob.getRandomVisibleItem()?.let { Take(it, worldState) }
                    in 3..4 -> mob.items.takeIf { it.isNotEmpty() }?.random()
                        ?.let { Drop(worldState, it) }

                    else -> Pass
                } ?: Pass
            }

            MobBehavior.AGGRESSIVE -> {
                when (roll20().value) {
                    in 0..18 -> mob.getFirstVisibleMob()?.let { AttackUnarmed(it, worldState) }
                    else -> Pass
                } ?: Pass
            }

            MobBehavior.WANDERER -> when (roll20().value) {
                0 -> Move.forceMove(worldState, Direction.NORTH)
                1 -> Move.forceMove(worldState, Direction.EAST)
                2 -> Move.forceMove(worldState, Direction.SOUTH)
                3 -> Move.forceMove(worldState, Direction.WEST)
                else -> Pass
            }

            else -> TODO()
        }
    }
}