package com.pbeagan.contextual.actions

import com.pbeagan.WorldState
import com.pbeagan.contextual.ItemData
import com.pbeagan.contextual.Mob
import com.pbeagan.contextual.actions.type.Action
import dev.patbeagan.b20.domain.flags.ItemFlags

class Drop(private val worldState: WorldState, private val item: ItemData) : Action() {

    override fun invoke(self: Mob) {
        item.locationInRoom = self.locationInRoom
        with(worldState) {
            self.currentRoom()?.items?.add(item)
        }
        self.items.remove(item)
        writer.sayToRoomOf(self).info("${self.nameStyled} dropped the ${item.nameStyled}")
    }

    companion object {
        fun getOrRetry(mob: Mob, itemName: String, worldState: WorldState): Action = mob.items
            .firstOrNull { itemData ->
                itemData.itemFlags.contains(ItemFlags.UNDROPPABLE)
                    .not() && itemData.names.any { name ->
                    itemName.let { name.startsWith(it) }
                }
            }
            ?.let { Drop(worldState , it) }
            ?: Retry("You're not holding that item...")
    }
}