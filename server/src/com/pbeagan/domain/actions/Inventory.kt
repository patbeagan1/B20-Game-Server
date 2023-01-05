package com.pbeagan.domain.actions

import com.pbeagan.domain.actions.type.Action
import com.pbeagan.domain.actions.type.FreeAction
import com.pbeagan.domain.Mob

class Inventory : Action(), FreeAction {
    override fun invoke(self: Mob) {
        writer.sayTo(self).info("Items in inventory:")
        writer.sayTo(self).info(self.items.joinToString("\n") { it.nameStyled }, true)
    }
}