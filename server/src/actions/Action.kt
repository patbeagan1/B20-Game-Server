package actions

import SampleData.mobs
import actions.actiondelegates.CombatDelegateProvider
import actions.actiondelegates.ItemDelegateProvider
import actions.actiondelegates.LookDelegateProvider
import actions.actiondelegates.MovementDelegateProvider
import com.pbeagan.models.Direction
import com.pbeagan.models.ItemData
import earlyMatches
import mob.Mob
import mob.attackFirstVisible
import mob.currentRoom
import mob.currentRoomOtherMobs
import writer.Writer

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
        fun attackOrRetry(i: List<String>, mob: Mob): Action {
            val target = i[2]
            return when {
                target.isEmpty() -> mob.attackFirstVisible(mobs)
                else ->
                    mob.currentRoomOtherMobs(mobs)
                        .firstOrNull { it.name.toLowerCase() == target }
                        ?.let { AttackMelee(it) }
            } ?: Retry("Looks like that mob isn't here...")
        }
    }
}

class Take(private val item: ItemData) : Action() {
    override fun invoke(self: Mob) = itemDelegateProvider.prepare(writer).take(self, item)

    companion object {
        fun getOrRetry(i: List<String>, mob: Mob): Action {
            val item = i.getOrNull(1)
            val firstOrNull = mob.currentRoom()?.items
                ?.firstOrNull { itemData ->
                    itemData.names.any { name ->
                        item?.let { name.earlyMatches(it) } ?: false
                    }
                }
            if (item == null) return Retry("What would you like to take?")
            if (firstOrNull == null) return Retry("That item isn't here...")
            return Take(firstOrNull)
        }
    }
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

object Unmatched : Action() {
    override operator fun invoke(self: Mob) = Unit
}

class Retry(val errorMsg: String) : Action() {
    override operator fun invoke(self: Mob) = Unit
}

class Examine(private val item: String) : Action(), FreeAction {
    override fun invoke(self: Mob) = itemDelegateProvider.prepare(writer).examine(self, item)

    companion object {
        fun getOrRetry(i: List<String>): Action = i
            .getOrNull(2)
            ?.let { Examine(it) }
            ?: Retry("What should I examine?")
    }
}

class Inventory : Action(), FreeAction {
    override fun invoke(self: Mob) = itemDelegateProvider.prepare(writer).inventory(self)
}
