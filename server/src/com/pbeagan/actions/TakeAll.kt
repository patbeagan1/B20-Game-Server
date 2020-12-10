package com.pbeagan.actions

import com.pbeagan.data.ItemFlags
import com.pbeagan.data.Mob
import com.pbeagan.data.currentRoom

class TakeAll : Action() {
    override fun invoke(self: Mob) = takeAll(self)

    private fun takeAll(self: Mob) {
        self.currentRoom()
            ?.items
            ?.filter { it.itemFlags.contains(ItemFlags.TAKEABLE) }
            ?.let { list ->
                self.currentRoom()?.items?.removeAll(list)
                self.items.addAll(list)
                list.forEach {
                    writer.sayToRoomOf(self).info("${self.name} picked up the ${it.names[0]}")
                }
            }
    }
}