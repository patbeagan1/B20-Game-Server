package com.pbeagan.contextual.actions

import com.pbeagan.WorldState
import com.pbeagan.contextual.actions.type.Action
import com.pbeagan.contextual.actions.type.FreeAction
import dev.patbeagan.b20.domain.types.AttackType
import com.pbeagan.contextual.Mob

class Settings(
    val worldState: WorldState,
    private val action: (Mob) -> Unit,
) : Action(), FreeAction {
    override operator fun invoke(self: Mob) {
        action(self)
        writer.sayTo(self).info("Settings were changed.")
    }

    companion object {
        fun getAttack(settingValue: String, worldState: WorldState): Action = when (settingValue) {
            "melee" -> Settings(worldState) {
                it.preferredAttack = AttackType.MELEE
            }

            "range", "ranged" -> Settings(worldState) {
                it.preferredAttack = AttackType.RANGED
            }

            "throw", "thrown" -> Settings(worldState) {
                it.preferredAttack = AttackType.THROWN
            }

            else -> Retry("Not a valid attack")
        }
    }
}