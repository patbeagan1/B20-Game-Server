package actions.actiondelegates

import actions.Doors
import actions.actiondelegates.LookDelegateProvider.LookDelegate
import mob.Mob
import mob.currentRoom

class LookDelegateProvider : ActionDelegateProvider<LookDelegate>() {
    interface LookDelegate {
        fun look(self: Mob, mobs: List<Mob>)
    }

    override fun build(): LookDelegate = LookDelegateImpl()

    private inner class LookDelegateImpl : LookDelegate {
        override fun look(self: Mob, mobs: List<Mob>) {
            self.currentRoom()?.let { room ->
                val roomDescription = room.descriptionLook
                val mobsInRoom = mobs
                    .filter { it.currentRoom() == room && self != it }
                    .joinToString("\n") {
                        "${it.name} is here - ${it.description}"
                    }
                val itemsInRoom = room
                    .items
                    .joinToString("\n") { it.descriptionInRoom }

                listOf(
                    roomDescription,
                    mobsInRoom,
                    itemsInRoom
                ).filter {
                    it.isNotBlank()
                }.joinToString("\n") {
                    it
                }.also {
                    writer.info(it)
                }

                Doors().also { it.writer = writer }.invoke(self)
            } ?: writer.error("No room found - how do you exist?")
        }
    }
}
