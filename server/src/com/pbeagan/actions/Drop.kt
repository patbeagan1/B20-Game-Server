package com.pbeagan.actions

import com.pbeagan.data.ItemData
import com.pbeagan.data.ItemFlags
import com.pbeagan.data.Mob
import com.pbeagan.data.currentRoom
import com.pbeagan.earlyMatches

class Drop(private val item: ItemData) : Action() {
    override fun invoke(self: Mob) = drop(self, item)

    fun drop(self: Mob, item: ItemData) {
        self.currentRoom()?.items?.add(item)
        self.items.remove(item)
        writer.sayToRoomOf(self).info("${self.name} dropped the ${item.names[0]}")
    }

    companion object {
        fun getOrRetry(mob: Mob, itemName: String): Action = mob.items
            .firstOrNull { itemData ->
                itemData.itemFlags.contains(ItemFlags.UNDROPPABLE).not() && itemData.names.any { name ->
                    itemName.let { name.earlyMatches(it) }
                }
            }
            ?.let { Drop(it) }
            ?: Retry("You're not holding that item...")
    }
}