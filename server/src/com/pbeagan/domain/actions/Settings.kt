package com.pbeagan.domain.actions

import com.pbeagan.domain.actions.type.Action
import com.pbeagan.domain.actions.type.FreeAction
import com.pbeagan.domain.types.AttackType
import com.pbeagan.domain.Mob

class Settings(
    private val action: (Mob) -> Unit
) : Action(), FreeAction {
    override operator fun invoke(self: Mob) {
        action(self)
        writer.sayTo(self).info("Settings were changed.")
    }

    companion object {
        fun getAttack(settingValue: String): Action = when (settingValue) {
            "melee" -> Settings {
                it.preferredAttack = AttackType.MELEE
            }
            "range", "ranged" -> Settings {
                it.preferredAttack = AttackType.RANGED
            }
            "throw", "thrown" -> Settings {
                it.preferredAttack = AttackType.THROWN
            }
            else -> Retry("Not a valid attack")
        }
    }
}