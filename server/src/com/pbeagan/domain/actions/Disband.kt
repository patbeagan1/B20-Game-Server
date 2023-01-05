package com.pbeagan.domain.actions

import com.pbeagan.domain.actions.type.Action
import com.pbeagan.domain.Mob

class Disband : Action() {
    override fun invoke(self: Mob) {
        self.party.clear()
    }
}