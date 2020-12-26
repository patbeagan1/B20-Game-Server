package com.pbeagan.actions

import com.pbeagan.data.Direction
import com.pbeagan.data.Mob
import com.pbeagan.data.currentRoom
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