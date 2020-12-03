package com.pbeagan.actions

import com.pbeagan.Direction
import com.pbeagan.ItemData
import com.pbeagan.ItemFlags
import com.pbeagan.ItemFlags.TAKEABLE
import com.pbeagan.ItemFlags.UNDROPPABLE
import com.pbeagan.SampleData.mobs
import com.pbeagan.actions.actiondelegates.CombatDelegateProvider
import com.pbeagan.actions.actiondelegates.ItemDelegateProvider
import com.pbeagan.actions.actiondelegates.LookDelegateProvider
import com.pbeagan.actions.actiondelegates.MovementDelegateProvider
import com.pbeagan.earlyMatches
import com.pbeagan.mob.Mob
import com.pbeagan.mob.currentRoom
import com.pbeagan.mob.currentRoomOtherMobs
import com.pbeagan.mob.getFirstVisibleMob
import com.pbeagan.writer.Writer
import rooms

sealed class Action {
    abstract operator fun invoke(self: Mob)
    lateinit var writer: Writer
    val combatDelegate = CombatDelegateProvider()
    val itemDelegateProvider = ItemDelegateProvider()
    val movementDelegateProvider = MovementDelegateProvider()
    val lookDelegateProvider = LookDelegateProvider()
}

interface FreeAction

class AttackMelee(private val target: Mob) : Action() {
    override operator fun invoke(self: Mob) = combatDelegate.prepare(writer).attackMelee(self, target)

    companion object {
        fun attackOrRetry(mob: Mob, target: String): Action = when {
            target.isEmpty() -> mob.getFirstVisibleMob()?.let { AttackMelee(it) }
            else ->
                mob.currentRoomOtherMobs(mobs)
                    .firstOrNull { it.name.toLowerCase() == target }
                    ?.let { AttackMelee(it) }
        } ?: Retry("Looks like that mob isn't here...")
    }
}

class Take(private val item: ItemData) : Action() {
    override fun invoke(self: Mob) = itemDelegateProvider.prepare(writer).take(self, item)

    companion object {
        fun getOrRetry(mob: Mob, itemName: String): Action {
            if (itemName == "all") return TakeAll()
            return mob.currentRoom()?.items
                ?.firstOrNull { itemData ->
                    itemData.itemFlags.contains(TAKEABLE) && itemData.names.any { name ->
                        itemName.let { name.earlyMatches(it) }
                    }
                }
                ?.let { Take(it) }
                ?: return Retry("That item isn't here...")
        }
    }
}

class Drop(private val item: ItemData) : Action() {
    override fun invoke(self: Mob) = itemDelegateProvider.prepare(writer).drop(self, item)

    companion object {
        fun getOrRetry(mob: Mob, itemName: String): Action = mob.items
            .firstOrNull { itemData ->
                itemData.itemFlags.contains(UNDROPPABLE).not() && itemData.names.any { name ->
                    itemName.let { name.earlyMatches(it) }
                }
            }
            ?.let { Drop(it) }
            ?: Retry("You're not holding that item...")
    }
}

class TakeAll : Action() {
    override fun invoke(self: Mob) = itemDelegateProvider.prepare(writer).takeAll(self)
}

class AttackRanged(private val target: Mob) : Action() {
    override fun invoke(self: Mob) = combatDelegate.prepare(writer).attackRanged(self, target)
}

class AttackThrow(private val target: Mob) : Action() {
    override fun invoke(self: Mob) = combatDelegate.prepare(writer).attackThrow(self, target)
}

class Look : Action(), FreeAction {
    override fun invoke(self: Mob) = lookDelegateProvider.prepare(writer).look(self, mobs)
}

class Doors : Action(), FreeAction {
    override fun invoke(self: Mob) = movementDelegateProvider.prepare(writer).doors(self)
}

class Move(private val direction: Direction) : Action() {
    override fun invoke(self: Mob) = movementDelegateProvider.prepare(writer).move(self, direction)
}

object Pass : Action() {
    override operator fun invoke(self: Mob) = Unit
}

class Repeat(private val action: Action) : Action() {
    override fun invoke(self: Mob) {
        if (action !is Repeat) {
            writer.sayToAll().info("Repeating the last action: ${action::class.java.simpleName}")
        }
        action.invoke(self)
    }

    val isFreeAction: Boolean
        get() = if (action is Repeat) action.isFreeAction else action is FreeAction
}

class Debug(private val target: String) : Action() {

    override fun invoke(self: Mob) {
        searchMobs() ?: searchMobItems() ?: searchRoomItems()
    }

    private fun searchRoomItems(): Unit? = rooms.map { it.value.items }.flatten().firstOrNull {
        it.nameMatches(target)
    }?.let {
        writer.sayToAll().info(it.toString())
    }

    private fun searchMobItems(): Unit? = mobs.map { it.items }.flatten().firstOrNull {
        it.nameMatches(target)
    }?.let {
        writer.sayToAll().info(it.toString())
    }

    private fun searchMobs(): Unit? = mobs.firstOrNull {
        it.name.toLowerCase() == target.toLowerCase()
    }?.let { currentMob ->
        writer.sayToAll().info(currentMob.toString())
        writer.sayToAll().info("hearts: ${currentMob.hearts}")
        writer.sayToAll().info("desc: ${currentMob.description}")
    }
}

object Inactive : Action() {
    override operator fun invoke(self: Mob) = writer.sayToRoomOf(self).info("${self.name} is inactive")
}

class Retry(private val errorMsg: String) : Action(), FreeAction {
    override operator fun invoke(self: Mob) = writer.sayTo(self).error(errorMsg)
}

class Examine(private val item: String) : Action(), FreeAction {
    override fun invoke(self: Mob) = itemDelegateProvider.prepare(writer).examine(self, item)
}

class Consume(private val item: ItemData) : Action() {
    override fun invoke(self: Mob) {
        item.flagHandlers[ItemFlags.CONSUMABLE]?.invoke(self)?.let {
            self.items.remove(item)
        }
    }

    companion object {
        fun getOrRetry(self: Mob, itemName: String) = self.items.firstOrNull {
            it.nameMatches(itemName)
        }?.let {
            Consume(it)
        } ?: Retry("You aren't holding that item")
    }
}

class Inventory : Action(), FreeAction {
    override fun invoke(self: Mob) = itemDelegateProvider.prepare(writer).inventory(self)
}
