package actions.actiondelegates

import actions.actiondelegates.MovementDelegateProvider.MovementDelegate
import com.pbeagan.models.Direction
import mob.Mob
import mob.currentRoom
import mob.look

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
                }
                ?.run {
                    writer.info("${self.name} moved ${direction.name}")
                    self.look(writer)
                }
                ?: writer.error("Sorry, can't go that way.")
        }

        override fun doors(self: Mob) {
            self.currentRoom()
                ?.directions
                ?.joinToString("\n") {
                    "${it.direction}: ${it.preview}"
                }?.also { writer.info(it) }
        }
    }
}
