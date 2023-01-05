package com.pbeagan.domain.actions

import com.pbeagan.domain.actions.type.Action
import com.pbeagan.domain.actions.type.FreeAction
import com.pbeagan.domain.Mob
import com.pbeagan.domain.currentRoom
import com.pbeagan.domain.currentRoomOtherMobs
import com.pbeagan.demo.SampleData.mobs
import com.pbeagan.util.startsWith

class Examine(private val targetName: String) : Action(), FreeAction {
    override fun invoke(self: Mob) {
        examineItem(self, targetName)
            ?: examineMob(self, targetName)
            ?: writer.sayTo(self).error("Looks like that isn't here...")
    }

    private fun examineMob(self: Mob, targetName: String): Unit? {
        val mob = self
            .currentRoomOtherMobs(mobs)
            .firstOrNull { it.nameBase.startsWith(targetName) }
            ?: return null
        return mob.ancestry?.let { ancestry ->
            writer.sayTo(self).info(mob.description.onExamine(ancestry))
        }
    }

    private fun examineItem(self: Mob, itemName: String): String? {
        val itemData =
            self.currentRoom()?.items?.firstOrNull {
                it.nameStartsWith(itemName)
            } ?: self.items.firstOrNull {
                it.nameStartsWith(itemName)
            }
        return itemData
            ?.descriptionOnExamination
            ?.also { writer.sayTo(self).info("${itemData.nameStyled}: $it") }
    }
}