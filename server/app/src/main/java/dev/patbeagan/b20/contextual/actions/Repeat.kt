package com.pbeagan.contextual.actions

import dev.patbeagan.b20.contextual.Mob
import com.pbeagan.contextual.actions.type.Action
import com.pbeagan.contextual.actions.type.FreeAction

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