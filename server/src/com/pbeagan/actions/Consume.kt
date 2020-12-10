package com.pbeagan.actions

import com.pbeagan.data.ItemData
import com.pbeagan.data.ItemFlags
import com.pbeagan.data.Mob

class Consume(private val item: ItemData) : Action() {
    override fun invoke(self: Mob) {
        item.flagHandlers[ItemFlags.CONSUMABLE]?.invoke(self)?.let {
            self.items.remove(item)
        }
    }

    companion object {
        fun getOrRetry(self: Mob, itemName: String): Action {
            val itemData = self.items.firstOrNull {
                it.nameMatches(itemName)
            } ?: return Retry("You're not holding that item")
            return Consume(itemData)
        }
    }
}