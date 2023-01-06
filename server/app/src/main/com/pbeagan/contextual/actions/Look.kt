package com.pbeagan.contextual.actions

import com.pbeagan.WorldState
import com.pbeagan.consolevision.Util
import com.pbeagan.contextual.Mob
import com.pbeagan.contextual.actions.type.Action
import com.pbeagan.contextual.actions.type.FreeAction

class Look(val worldState: WorldState) : Action(), FreeAction {
    override fun invoke(self: Mob) = look(self, worldState.mobs)

    private fun look(self: Mob, mobs: List<Mob>) = with(worldState) {
        self.currentRoom()?.let { room ->

            writer.sayTo(self).look(room.descriptionLook)

            // Mobs in the current room
            self.currentRoomOtherMobs(mobs).joinToString("\n") {
                "${it.nameStyled} (${
                    Util.distanceManhattan(
                        self.locationInRoom,
                        it.locationInRoom
                    )
                } paces): ${it.description.onLook(it.behavior)}"
            }.takeIf { it.isNotBlank() }?.also {
                writer.sayTo(self).info(it)
            }

            // Items in the current room
            room.items
                .map {
                    "(${
                        Util.distanceManhattan(
                            self.locationInRoom,
                            it.locationInRoom
                        )
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