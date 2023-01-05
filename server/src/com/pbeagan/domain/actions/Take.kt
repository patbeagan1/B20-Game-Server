package com.pbeagan.domain.actions

import com.pbeagan.domain.actions.type.Action
import com.pbeagan.consolevision.Util
import com.pbeagan.domain.ItemData
import com.pbeagan.domain.flags.ItemFlags
import com.pbeagan.domain.Mob
import com.pbeagan.domain.currentRoom
import com.pbeagan.domain.takeIfItIsInRangeOf
import com.pbeagan.util.startsWith

class Take(private val item: ItemData) : Action() {
    override fun invoke(self: Mob) {
        self.currentRoom()
            ?.items
            ?.firstOrNull { it == item }
            ?.takeIf { Util.distance(self.locationInRoom, it.locationInRoom) <= 1 }
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
                ?.takeIfItIsInRangeOf(mob, 1)
                ?.let { Take(it) }
                ?: return Retry("That item isn't here...")
        }
    }
}