package com.pbeagan.contextual.actions

import com.pbeagan.consolevision.Util
import com.pbeagan.contextual.ItemData
import com.pbeagan.contextual.Mob
import com.pbeagan.contextual.actions.type.Action
import com.pbeagan.contextual.currentRoom
import com.pbeagan.util.startsWith
import dev.patbeagan.b20.domain.flags.ItemFlags
import dev.patbeagan.b20.domain.takeIfItIsInRangeOf

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