package com.pbeagan.domain.actions

import com.pbeagan.domain.actions.type.Action
import com.pbeagan.domain.actions.type.FreeAction
import com.pbeagan.domain.Mob

class Repeat(private val action: Action) : Action() {
    override fun invoke(self: Mob) {
        if (action !is Repeat) {
            writer.sayTo(self).info("Repeating the last action: ${action::class.java.simpleName}")
        }
        action.invoke(self)
    }

    val isRepeatFreeAction: Boolean
        get() = if (action is Repeat) action.isRepeatFreeAction else action is FreeAction
}