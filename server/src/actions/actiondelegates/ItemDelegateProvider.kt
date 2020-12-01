package actions.actiondelegates

import actions.actiondelegates.ItemDelegateProvider.ItemDelegate
import com.pbeagan.models.ItemData
import earlyMatches
import mob.Mob
import mob.currentRoom

class ItemDelegateProvider : ActionDelegateProvider<ItemDelegate>() {
    interface ItemDelegate {
        fun inventory(self: Mob)
        fun examine(self: Mob, item: String)
        fun take(self: Mob, item: ItemData)
    }

    override fun build(): ItemDelegate = ItemDelegateImpl()

    private inner class ItemDelegateImpl : ItemDelegate {
        override fun inventory(self: Mob) {
            writer.info("Items in inventory:")
            writer.info(self.items.joinToString("\n\t") { it.names[0] })
        }

        override fun examine(self: Mob, item: String) {
            self.currentRoom()
                ?.items
                ?.firstOrNull { itemData ->
                    itemData.names.any { item.earlyMatches(it) }
                }?.descriptionOnExamination
                ?.also { writer.info(it) }
                ?: writer.error("Looks like that item isn't here...")
        }

        override fun take(self: Mob, item: ItemData) {
            self.currentRoom()
                ?.items
                ?.firstOrNull { it == item }
                ?.let {
                    self.currentRoom()?.items?.remove(it)
                    self.items.add(it)
                    writer.info("${self.name} picked up the ${it.names[0]}")
                }
        }
    }
}