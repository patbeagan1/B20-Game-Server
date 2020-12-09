package com.pbeagan.actions.actiondelegates

import com.pbeagan.actions.Doors
import com.pbeagan.actions.actiondelegates.LookDelegateProvider.LookDelegate
import com.pbeagan.data.Mob
import com.pbeagan.data.currentRoom

class LookDelegateProvider : ActionDelegateProvider<LookDelegate>() {
    interface LookDelegate {
        fun look(self: Mob, mobs: List<Mob>)
    }

    override fun build(): LookDelegate = LookDelegateImpl()

    private inner class LookDelegateImpl : LookDelegate {
        override fun look(self: Mob, mobs: List<Mob>) {
            self.currentRoom()?.let { room ->

                writer.sayTo(self).system(room.descriptionLook)

                listOf(
                    // Mobs in the current room
                    mobs.filter {
                        it.currentRoom() == room && self != it
                    }.joinToString("\n") {
                        "${it.name} is here - ${it.description}"
                    },
                    // Items in the current room
                    room.items.joinToString("\n") { it.descriptionInRoom }
                ).filter {
                    it.isNotBlank()
                }.joinToString("\n") {
                    it
                }.also {
                    writer.sayTo(self).info(it)
                }

                Doors().also { it.writer = writer }.invoke(self)
            } ?: writer.sayTo(self).error("No room found - how do you exist?")
        }
    }
}
