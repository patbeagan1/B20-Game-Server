package dev.patbeagan.b20.contextual.actions

import com.pbeagan.contextual.actions.Doors
import dev.patbeagan.b20.contextual.Mob
import com.pbeagan.contextual.actions.type.Action
import com.pbeagan.contextual.actions.type.FreeAction
import dev.patbeagan.b20.WorldState

class Look(val worldState: WorldState) : Action(), FreeAction {
    override fun invoke(self: Mob) = look(self, worldState.mobs)

    private fun look(self: Mob, mobs: List<Mob>) = with(worldState) {
        self.currentRoom()?.let { room ->

            writer.sayTo(self).look(room.descriptionLook)

            // Mobs in the current room
            self.currentRoomOtherMobs(mobs).joinToString("\n") {
                "${it.nameStyled} (${
                    self.locationInRoom.distanceManhattanFrom(it.locationInRoom)
                } paces): ${it.description.onLook(it.behavior)}"
            }.takeIf { it.isNotBlank() }?.also {
                writer.sayTo(self).info(it)
            }

            // Items in the current room
            room.items
                .map {
                    "(${
                        self.locationInRoom.distanceManhattanFrom(it.locationInRoom)
                    } paces) ${it.nameStyled} : ${it.descriptionInRoom}"
                }
                .sorted()
                .joinToString("\n")
                .takeIf { it.isNotBlank() }
                ?.also {
                    writer.sayTo(self).info(it)
                }

            // Doors in the room
            Doors(worldState).also { it.writer = writer }.invoke(self)

        } ?: writer.sayTo(self).error("No room found - how do you exist?")
    }
}