package com.pbeagan.domain.actions

import com.pbeagan.domain.actions.type.Action
import com.pbeagan.domain.actions.type.FreeAction
import com.pbeagan.domain.types.Direction
import com.pbeagan.domain.Mob
import com.pbeagan.domain.currentRoom
import com.pbeagan.demo.SampleData.rooms

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