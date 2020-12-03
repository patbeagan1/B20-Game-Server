package com.pbeagan.actions.actiondelegates

import com.pbeagan.ItemData
import com.pbeagan.ItemFlags.TAKEABLE
import com.pbeagan.actions.actiondelegates.ItemDelegateProvider.ItemDelegate
import com.pbeagan.mob.Mob
import com.pbeagan.mob.currentRoom

class ItemDelegateProvider : ActionDelegateProvider<ItemDelegate>() {
    interface ItemDelegate {
        fun inventory(self: Mob)
        fun examine(self: Mob, item: String)
        fun take(self: Mob, item: ItemData)
        fun takeAll(self: Mob)
        fun drop(self: Mob, item: ItemData)
    }

    override fun build(): ItemDelegate = ItemDelegateImpl()

    private inner class ItemDelegateImpl : ItemDelegate {
        override fun inventory(self: Mob) {
            writer.sayTo(self).info("Items in inventory:")
            writer.sayTo(self).info(self.items.joinToString("\n\t") { it.names[0] }, true)
        }

        override fun examine(self: Mob, item: String) {
            self.currentRoom()
                ?.items
                ?.firstOrNull { it.nameMatches(item) }
                ?.descriptionOnExamination
                ?.also { writer.sayTo(self).info(it) }
                ?: writer.sayTo(self).error("Looks like that item isn't here...")
        }

        override fun take(self: Mob, item: ItemData) {
            self.currentRoom()
                ?.items
                ?.firstOrNull { it == item }
                ?.let {
                    self.items.add(it)
                    self.currentRoom()?.items?.remove(it)
                    writer.sayToRoomOf(self).info("${self.name} picked up the ${it.names[0]}")
                }
        }

        override fun takeAll(self: Mob) {
            self.currentRoom()
                ?.items
                ?.filter { it.itemFlags.contains(TAKEABLE) }
                ?.let { list ->
                    self.currentRoom()?.items?.removeAll(list)
                    self.items.addAll(list)
                    list.forEach {
                        writer.sayToRoomOf(self).info("${self.name} picked up the ${it.names[0]}")
                    }
                }
        }

        override fun drop(self: Mob, item: ItemData) {
            self.currentRoom()?.items?.add(item)
            self.items.remove(item)
            writer.sayToRoomOf(self).info("${self.name} dropped the ${item.names[0]}")
        }
    }
}
