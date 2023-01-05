package com.pbeagan.contextual.actions

import com.pbeagan.contextual.actions.type.Action
import dev.patbeagan.b20.domain.flags.ItemFlags
import com.pbeagan.contextual.Mob
import com.pbeagan.contextual.currentRoom
import dev.patbeagan.b20.domain.takeIfItIsInRangeOf

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