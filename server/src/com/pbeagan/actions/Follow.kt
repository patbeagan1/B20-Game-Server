package com.pbeagan.actions

import com.pbeagan.data.Mob

class Follow(val target: Mob): Action() {
    override fun invoke(self: Mob) {
        target.party.add(self)
    }
}