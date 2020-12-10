package com.pbeagan.actions

import com.pbeagan.data.Mob

object Pass : Action() {
    override operator fun invoke(self: Mob) = Unit
}