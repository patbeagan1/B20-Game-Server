package com.pbeagan.contextual.actions

import com.pbeagan.contextual.Mob
import com.pbeagan.contextual.actions.type.Action

class Follow(val target: Mob): Action() {
    override fun invoke(self: Mob) {
        target.party.add(self)
    }
}