package com.pbeagan.domain.actions

import com.pbeagan.domain.actions.type.Action
import com.pbeagan.demo.SampleData
import com.pbeagan.domain.ItemData
import com.pbeagan.domain.flags.ItemFlags
import com.pbeagan.domain.Mob
import com.pbeagan.domain.currentRoomOtherMobs
import com.pbeagan.util.startsWith

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