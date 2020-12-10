package com.pbeagan.actions

import com.pbeagan.SampleData
import com.pbeagan.data.ItemData
import com.pbeagan.data.ItemFlags
import com.pbeagan.data.Mob
import com.pbeagan.data.currentRoomOtherMobs
import com.pbeagan.earlyMatches

class Give(private val target: Mob, private val item: ItemData) : Action() {
    override fun invoke(self: Mob) {
        self.items.remove(item)
        target.items.add(item)
        writer.sayTo(self, target).info("${self.name} gave ${target.name} a ${item.names[0]}")
    }

    companion object {
        fun getOrRetry(self: Mob, target: String, itemName: String): Action {
            val itemData = self
                .items.firstOrNull { it.nameMatches(itemName) }
                ?: return Retry("You aren't holding that item")

            if (itemData.itemFlags.contains(ItemFlags.UNDROPPABLE)) {
                return Retry("You can't drop that item")
            }

            val mob = self
                .currentRoomOtherMobs(SampleData.mobs)
                .also { println(it) }
                .firstOrNull { target.earlyMatches(it.name) }
                ?: return Retry("$target isn't here")
            return Give(mob, itemData)
        }
    }
}