package com.pbeagan.domain.actions

import com.pbeagan.domain.actions.type.Action
import com.pbeagan.domain.ItemData
import com.pbeagan.domain.flags.ItemFlags
import com.pbeagan.domain.Mob

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
            } ?: return com.pbeagan.domain.actions.Retry("You're not holding that item")
            return com.pbeagan.domain.actions.Consume(itemData)
        }
    }
}