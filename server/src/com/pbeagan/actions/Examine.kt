package com.pbeagan.actions

import com.pbeagan.SampleData.mobs
import com.pbeagan.data.Mob
import com.pbeagan.data.currentRoom
import com.pbeagan.data.currentRoomOtherMobs
import com.pbeagan.earlyMatches

class Examine(private val targetName: String) : Action(), FreeAction {
    override fun invoke(self: Mob) {
        examineItem(self, targetName)
            ?: examineMob(self, targetName)
            ?: writer.sayTo(self).error("Looks like that isn't here...")
    }

    private fun examineMob(self: Mob, targetName: String): Unit? {
        val mob = self
            .currentRoomOtherMobs(mobs)
            .firstOrNull { it.name.earlyMatches(targetName) }
            ?: return null

        return mob.ancestry?.let { ancestry ->
            writer.sayTo(self).info(mob.description.onExamine(ancestry))
        }
    }

    private fun examineItem(self: Mob, itemName: String) = self.currentRoom()
        ?.items
        ?.firstOrNull { it.nameMatches(itemName) }
        ?.descriptionOnExamination
        ?.also { writer.sayTo(self).info(it) }
}