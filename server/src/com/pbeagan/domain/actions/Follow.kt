package com.pbeagan.domain.actions

import com.pbeagan.domain.actions.type.Action
import com.pbeagan.domain.Mob

class Follow(val target: Mob): Action() {
    override fun invoke(self: Mob) {
        target.party.add(self)
    }
}