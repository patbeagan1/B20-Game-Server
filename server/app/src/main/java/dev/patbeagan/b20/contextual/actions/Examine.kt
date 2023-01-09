package com.pbeagan.contextual.actions

import dev.patbeagan.b20.contextual.Mob
import com.pbeagan.contextual.actions.type.Action
import com.pbeagan.contextual.actions.type.FreeAction
import dev.patbeagan.b20.util.commonPrefixWithIgnoreCase
import dev.patbeagan.b20.WorldState

class Examine(private val worldState: WorldState, private val targetName: String) : Action(), FreeAction {
    override fun invoke(self: Mob) {
        examineItem(self, targetName)
            ?: examineMob(self, targetName)
            ?: writer.sayTo(self).error("Looks like that isn't here...")
    }

    private fun examineMob(self: Mob, targetName: String): Unit? = with(worldState){
        val mob = self
            .currentRoomOtherMobs(mobs)
            .firstOrNull { it.nameBase.commonPrefixWithIgnoreCase(targetName) }
            ?: return null
        return mob.ancestry?.let { ancestry ->
            writer.sayTo(self).info(mob.description.onExamine(ancestry))
        }
    }

    private fun examineItem(self: Mob, itemName: String): String? = with(worldState) {
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