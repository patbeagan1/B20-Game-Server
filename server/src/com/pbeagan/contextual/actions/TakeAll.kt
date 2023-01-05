package com.pbeagan.contextual.actions

import com.pbeagan.WorldState
import com.pbeagan.contextual.Mob
import com.pbeagan.contextual.actions.type.Action
import dev.patbeagan.b20.domain.flags.ItemFlags
import dev.patbeagan.b20.domain.takeIfItIsInRangeOf

class TakeAll(val worldState: WorldState) : Action() {
    override fun invoke(self: Mob) = takeAll(self)

    private fun takeAll(self: Mob) {
        with(worldState) {
            self.currentRoom()
                ?.items
                ?.filter { it.itemFlags.contains(ItemFlags.TAKEABLE) }
                ?.mapNotNull { it.takeIfItIsInRangeOf(self, 1) }
                ?.let { list ->
                    self.currentRoom()?.items?.removeAll(list)
                    self.items.addAll(list)
                    list.forEach {
                        writer.sayToRoomOf(self)
                            .info("${self.nameStyled} picked up the ${it.nameStyled}")
                    }
                }
        }
    }
}