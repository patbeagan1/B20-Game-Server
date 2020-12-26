package com.pbeagan.actions

import com.pbeagan.data.Mob
import com.pbeagan.data.currentRoom
import com.pbeagan.data.currentRoomOtherMobs
import mobs

class Look : Action(), FreeAction {
    override fun invoke(self: Mob) = look(self, mobs)

    private fun look(self: Mob, mobs: List<Mob>) = self.currentRoom()?.let { room ->

        writer.sayTo(self).look(room.descriptionLook)

        // Mobs in the current room
        self.currentRoomOtherMobs(mobs).joinToString("\n") {
            "${it.nameStyled} is here - ${it.description.onLook(it.behavior)}"
        }.takeIf { it.isNotBlank() }?.also {
            writer.sayTo(self).info(it)
        }

        // Items in the current room
        room.items.joinToString("\n") {
            it.descriptionInRoom
        }.takeIf { it.isNotBlank() }?.also {
            writer.sayTo(self).info(it)
        }

        // Doors in the room
        Doors().also { it.writer = writer }.invoke(self)

    } ?: writer.sayTo(self).error("No room found - how do you exist?")

}