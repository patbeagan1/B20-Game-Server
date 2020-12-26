package com.pbeagan.actions

import com.pbeagan.data.Mob
import com.pbeagan.data.currentRoom

class Doors : Action(), FreeAction {
    override fun invoke(self: Mob) = doors(self)
    private fun doors(self: Mob) {
        self.currentRoom()
            ?.exits
            ?.forEach {
                writer.sayTo(self).door(it.direction, it.preview)
            }
    }
}