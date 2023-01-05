package com.pbeagan.domain.actions

import com.pbeagan.domain.actions.type.Action
import com.pbeagan.domain.flags.ItemFlags
import com.pbeagan.domain.Mob
import com.pbeagan.domain.currentRoom
import com.pbeagan.domain.takeIfItIsInRangeOf

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