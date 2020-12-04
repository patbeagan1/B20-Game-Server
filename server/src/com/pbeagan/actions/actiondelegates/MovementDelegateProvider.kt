package com.pbeagan.actions.actiondelegates

import com.pbeagan.Direction
import com.pbeagan.MobBehavior
import com.pbeagan.actions.Look
import com.pbeagan.actions.actiondelegates.MovementDelegateProvider.MovementDelegate
import com.pbeagan.mob.Mob
import com.pbeagan.mob.currentRoom

class MovementDelegateProvider : ActionDelegateProvider<MovementDelegate>() {
    interface MovementDelegate {
        fun move(self: Mob, direction: Direction)
        fun doors(self: Mob)
    }

    override fun build(): MovementDelegate = MovementDelegateImpl()

    private inner class MovementDelegateImpl : MovementDelegate {
        override fun move(self: Mob, direction: Direction) {
            self.currentRoom()
                ?.directions
                ?.firstOrNull { it.direction == direction }
                ?.let { room ->
                    self.location = room.destinationID
                    val visited = self.visited
                    if (!visited.contains(self.location)) {
                        visited.add(self.location)
                    }
                }?.run {
                    writer.sayToRoomOf(self).move("${self.name} moved ${direction.name}")
                    if (self.behavior == MobBehavior.PLAYER) {
                        Look().also { it.writer = writer }(self)
                    }
                }
                ?: if (self.behavior == MobBehavior.PLAYER) {
                    writer.sayTo(self).error("Sorry, can't go that way.")
                }
        }

        override fun doors(self: Mob) {
            self.currentRoom()
                ?.directions
                ?.joinToString("\n") {
                    "${it.direction}: ${it.preview}"
                }?.also { writer.sayTo(self).info(it) }
        }
    }
}
