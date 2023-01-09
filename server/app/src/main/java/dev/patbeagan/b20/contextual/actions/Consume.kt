package com.pbeagan.contextual.actions

import dev.patbeagan.b20.contextual.ItemData
import dev.patbeagan.b20.contextual.Mob
import com.pbeagan.contextual.actions.type.Action
import dev.patbeagan.b20.domain.flags.ItemFlags

class Consume(private val item: ItemData) : Action() {
    override fun invoke(self: Mob) {
        item.flagHandlers[ItemFlags.CONSUMABLE]?.invoke(self)?.let {
            self.items.remove(item)
        }
    }

    companion object {
        fun getOrRetry(self: Mob, itemName: String): Action {
            val itemData = self.items.firstOrNull {
                it.nameStartsWith(itemName)
            } ?: return Retry("You're not holding that item")
            return Consume(itemData)
        }
    }
}