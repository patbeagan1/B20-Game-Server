package com.pbeagan.actions

import com.pbeagan.data.Direction
import com.pbeagan.data.Mob
import com.pbeagan.data.MobBehavior
import com.pbeagan.data.currentRoom

class Move(private val direction: Direction) : Action() {
    override fun invoke(self: Mob) = move(self, direction)

    private fun move(self: Mob, direction: Direction) {
        self.currentRoom()
            ?.directions
            ?.firstOrNull { it.direction == direction }
            ?.let { room ->
                writer.sayToRoomOf(self).move("${self.name} left ${direction.name}")
                self.location = room.destinationID
                writer.sayToRoomOf(self).move("${self.name} arrived from ${direction.inverse().name}")

                recordVisit(self)

                if (self.isPlayer) {
                    Look().also { it.writer = writer }(self)
                }
            }
            ?: if (self.isPlayer) {
                writer.sayTo(self).error("Sorry, can't go that way.")
            }
    }

    private fun recordVisit(self: Mob) {
        val visited = self.visited
        if (!visited.contains(self.location)) {
            visited.add(self.location)
        }
    }
}