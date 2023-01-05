package com.pbeagan.contextual.actions

import com.pbeagan.contextual.actions.type.Action
import com.pbeagan.contextual.Mob

object Pass : Action() {
    override operator fun invoke(self: Mob) = Unit
}