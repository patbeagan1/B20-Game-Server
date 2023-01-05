package com.pbeagan.domain.actions

import com.pbeagan.domain.actions.type.Action
import com.pbeagan.domain.ItemData
import com.pbeagan.domain.flags.ItemFlags
import com.pbeagan.domain.Mob
import com.pbeagan.domain.currentRoom
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