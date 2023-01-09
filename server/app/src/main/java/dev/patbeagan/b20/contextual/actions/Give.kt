package com.pbeagan.contextual.actions

import dev.patbeagan.b20.contextual.ItemData
import dev.patbeagan.b20.contextual.Mob
import com.pbeagan.contextual.actions.type.Action
import dev.patbeagan.b20.util.commonPrefixWithIgnoreCase
import dev.patbeagan.b20.WorldState
import dev.patbeagan.b20.domain.flags.ItemFlags

class Give(
    private val target: Mob,
    private val item: ItemData,
) : Action() {
    override fun invoke(self: Mob) {
        self.items.remove(item)
        target.items.add(item)
        writer.sayTo(self, target)
            .info("${self.nameStyled} gave ${target.nameStyled} a ${item.nameStyled}")
    }

    companion object {
        fun getOrRetry(
            self: Mob, target: String, itemName: String, worldState: WorldState,
        ): Action {
            val itemData = self
                .items.firstOrNull { it.nameStartsWith(itemName) }
                ?: return Retry("You aren't holding that item")

            if (itemData.itemFlags.contains(ItemFlags.UNDROPPABLE)) {
                return Retry("You can't drop that item")
            }
            with(worldState) {
                val mob = self
                    .currentRoomOtherMobs(mobs)
                    .also { println(it) }
                    .firstOrNull { target.commonPrefixWithIgnoreCase(it.nameBase) }
                    ?: return Retry("$target isn't here")
                return Give(mob, itemData)
            }
        }
    }
}