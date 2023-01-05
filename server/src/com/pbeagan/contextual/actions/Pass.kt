package com.pbeagan.contextual.actions

import com.pbeagan.contextual.Mob
import com.pbeagan.contextual.actions.type.Action

object Pass : Action() {
    override operator fun invoke(self: Mob) = Unit
}