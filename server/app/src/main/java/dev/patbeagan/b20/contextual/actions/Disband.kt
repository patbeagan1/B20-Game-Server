package com.pbeagan.contextual.actions

import dev.patbeagan.b20.contextual.Mob
import com.pbeagan.contextual.actions.type.Action

class Disband : Action() {
    override fun invoke(self: Mob) {
        self.party.clear()
    }
}