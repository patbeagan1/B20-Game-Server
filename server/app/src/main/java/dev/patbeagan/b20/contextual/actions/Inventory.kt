package com.pbeagan.contextual.actions

import dev.patbeagan.b20.contextual.Mob
import com.pbeagan.contextual.actions.type.Action
import com.pbeagan.contextual.actions.type.FreeAction

class Inventory : Action(), FreeAction {
    override fun invoke(self: Mob) {
        writer.sayTo(self).info("Items in inventory:")
        writer.sayTo(self).info(self.items.joinToString("\n") { it.nameStyled }, true)
    }
}