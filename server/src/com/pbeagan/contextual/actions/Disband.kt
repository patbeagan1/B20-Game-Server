package com.pbeagan.contextual.actions

import com.pbeagan.contextual.actions.type.Action
import com.pbeagan.contextual.Mob

class Disband : Action() {
    override fun invoke(self: Mob) {
        self.party.clear()
    }
}