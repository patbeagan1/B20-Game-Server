package com.pbeagan.domain.actions

import com.pbeagan.domain.actions.type.Action
import com.pbeagan.domain.Mob

object Pass : Action() {
    override operator fun invoke(self: Mob) = Unit
}