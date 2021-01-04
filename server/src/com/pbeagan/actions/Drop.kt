package com.pbeagan.actions

import com.pbeagan.data.ItemData
import com.pbeagan.data.ItemFlags
import com.pbeagan.data.Mob
import com.pbeagan.data.currentRoom
import com.pbeagan.util.startsWith

class Drop(private val item: ItemData) : Action() {

    override fun invoke(self: Mob) {
        item.locationInRoom = self.locationInRoom
        self.currentRoom()?.items?.add(item)
        self.items.remove(item)
        writer.sayToRoomOf(self).info("${self.nameStyled} dropped the ${item.nameStyled}")
    }

    companion object {
        fun getOrRetry(mob: Mob, itemName: String): Action = mob.items
            .firstOrNull { itemData ->
                itemData.itemFlags.contains(ItemFlags.UNDROPPABLE).not() && itemData.names.any { name ->
                    itemName.let { name.startsWith(it) }
                }
            }
            ?.let { Drop(it) }
            ?: Retry("You're not holding that item...")
    }
}