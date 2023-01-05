package com.pbeagan.contextual.actions

import com.pbeagan.contextual.actions.type.Action
import com.pbeagan.contextual.actions.type.FreeAction
import com.pbeagan.contextual.Mob

class Inventory : Action(), FreeAction {
    override fun invoke(self: Mob) {
        writer.sayTo(self).info("Items in inventory:")
        writer.sayTo(self).info(self.items.joinToString("\n") { it.nameStyled }, true)
    }
}