package com.pbeagan.actions

import com.pbeagan.data.Mob

class AllyWith(val target: Mob): Action(), FreeAction {
    override fun invoke(self: Mob) {
        self.allies.add(target)
    }
}