package com.pbeagan.contextual.actions

import dev.patbeagan.b20.contextual.Mob
import com.pbeagan.contextual.actions.type.Action
import dev.patbeagan.b20.WorldState
import dev.patbeagan.b20.contextual.actions.AttackUnarmed
import dev.patbeagan.b20.domain.types.AttackType

class AttackDefault(val target: String, val worldState: WorldState) : Action() {
    override fun invoke(self: Mob) {
        with(worldState) {
            val target = when {
                target.isEmpty() -> self.getFirstVisibleMob()
                else -> self.target(target)
            }
            val attack = target?.let {
                when (self.preferredAttack) {
                    AttackType.MELEE -> AttackUnarmed(it, worldState)
                    AttackType.RANGED -> AttackRanged(it, worldState)
                    AttackType.THROWN -> AttackThrow(it, worldState)
                    AttackType.MAGIC -> TODO()
                }
            }
            attack ?: Retry("Looks like that mob isn't here...")
        }
            .also { it.writer = writer }
            .invoke(self)
    }
}