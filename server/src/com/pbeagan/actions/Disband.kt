package com.pbeagan.actions

import com.pbeagan.data.Mob

class Disband : Action() {
    override fun invoke(self: Mob) {
        self.party.clear()
    }
}