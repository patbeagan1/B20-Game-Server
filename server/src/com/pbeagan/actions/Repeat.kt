package com.pbeagan.actions

import com.pbeagan.data.Mob

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