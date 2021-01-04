package com.pbeagan.actions

import com.pbeagan.data.ItemFlags
import com.pbeagan.data.Mob
import com.pbeagan.data.currentRoom
import com.pbeagan.data.takeIfItIsInRangeOf

class TakeAll : Action() {
    override fun invoke(self: Mob) = takeAll(self)

    private fun takeAll(self: Mob) {
        self.currentRoom()
            ?.items
            ?.filter { it.itemFlags.contains(ItemFlags.TAKEABLE) }
            ?.mapNotNull { it.takeIfItIsInRangeOf(self, 1) }
            ?.let { list ->
                self.currentRoom()?.items?.removeAll(list)
                self.items.addAll(list)
                list.forEach {
                    writer.sayToRoomOf(self).info("${self.nameStyled} picked up the ${it.nameStyled}")
                }
            }
    }
}