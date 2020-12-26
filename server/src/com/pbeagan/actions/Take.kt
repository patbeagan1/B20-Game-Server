package com.pbeagan.actions

import com.pbeagan.data.ItemData
import com.pbeagan.data.ItemFlags
import com.pbeagan.data.Mob
import com.pbeagan.data.currentRoom
import com.pbeagan.util.startsWith

class Take(private val item: ItemData) : Action() {
    override fun invoke(self: Mob) {
        self.currentRoom()
                ?.items
                ?.firstOrNull { it == item }
                ?.let {
                    self.items.add(it)
                    self.currentRoom()?.items?.remove(it)
                    writer.sayToRoomOf(self).info("${self.nameStyled} picked up the ${it.nameStyled}")
                }
    }

    companion object {
        fun getOrRetry(mob: Mob, itemName: String): Action {
            if (itemName == "all") return TakeAll()
            return mob.currentRoom()?.items
                ?.firstOrNull { itemData ->
                    itemData.itemFlags.contains(ItemFlags.TAKEABLE) && itemData.names.any { name ->
                        itemName.let { name.startsWith(it) }
                    }
                }
                ?.let { Take(it) }
                ?: return Retry("That item isn't here...")
        }
    }
}