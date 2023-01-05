package com.pbeagan.contextual.actions

import com.pbeagan.contextual.Mob
import com.pbeagan.contextual.actions.type.Action
import com.pbeagan.contextual.actions.type.FreeAction
import com.pbeagan.contextual.currentRoom
import com.pbeagan.demo.SampleData.rooms
import dev.patbeagan.b20.domain.types.Direction

class Doors : Action(), FreeAction {
    override fun invoke(self: Mob) = doors(self)
    private fun doors(self: Mob) {
        self.currentRoom()?.exits?.run {
            Direction.values().mapNotNull { direction ->
                get(direction)
                    ?.destinationID.let { rooms[it] }
                    ?.descriptionPreview?.invoke(direction)
                    ?.let { direction to it }
            }.forEach {
                writer.sayTo(self).door(it.first, it.second)
            }
        }
    }
}