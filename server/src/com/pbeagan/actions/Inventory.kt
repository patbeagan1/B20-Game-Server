package com.pbeagan.actions

import com.pbeagan.data.Mob

class Inventory : Action(), FreeAction {
    override fun invoke(self: Mob) {
        writer.sayTo(self).info("Items in inventory:")
        writer.sayTo(self).info(self.items.joinToString("\n") { it.names[0] }, true)
    }
}