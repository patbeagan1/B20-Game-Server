package com.pbeagan.contextual.actions

import com.pbeagan.contextual.actions.type.Action
import com.pbeagan.demo.SampleData
import com.pbeagan.contextual.ItemData
import dev.patbeagan.b20.domain.flags.ItemFlags
import com.pbeagan.contextual.Mob
import com.pbeagan.contextual.currentRoomOtherMobs

class Give(private val target: Mob, private val item: ItemData) : Action() {
    override fun invoke(self: Mob) {
        self.items.remove(item)
        target.items.add(item)
        writer.sayTo(self, target).info("${self.nameStyled} gave ${target.nameStyled} a ${item.nameStyled}")
    }

    companion object {
        fun getOrRetry(self: Mob, target: String, itemName: String): Action {
            val itemData = self
                .items.firstOrNull { it.nameStartsWith(itemName) }
                ?: return Retry("You aren't holding that item")

            if (itemData.itemFlags.contains(ItemFlags.UNDROPPABLE)) {
                return Retry("You can't drop that item")
            }

            val mob = self
                .currentRoomOtherMobs(SampleData.mobs)
                .also { println(it) }
                .firstOrNull { target.startsWith(it.nameBase) }
                ?: return Retry("$target isn't here")
            return Give(mob, itemData)
        }
    }
}