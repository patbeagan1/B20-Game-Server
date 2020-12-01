package actions

import SampleData.mobs
import actions.actiondelegates.CombatDelegateProvider
import actions.actiondelegates.ItemDelegateProvider
import actions.actiondelegates.LookDelegateProvider
import actions.actiondelegates.MovementDelegateProvider
import com.pbeagan.models.Direction
import com.pbeagan.models.ItemData
import mob.Mob
import writer.Writer

sealed class Action {
    abstract operator fun invoke(self: Mob)
//    abstract fun match(s: String): ((List<String>) -> Action)
    lateinit var writer: Writer
    val combatDelegate = CombatDelegateProvider()
    val itemDelegateProvider = ItemDelegateProvider()
    val movementDelegateProvider = MovementDelegateProvider()
    val lookDelegateProvider = LookDelegateProvider()
}

class AttackMelee(private val target: Mob) : Action() {
    override operator fun invoke(self: Mob) = combatDelegate.prepare(writer).attackMelee(self, target)
}

class Take(private val item: ItemData) : Action() {
    override fun invoke(self: Mob) = itemDelegateProvider.prepare(writer).take(self, item)
}

class AttackRanged(private val target: Mob) : Action() {
    override fun invoke(self: Mob) = combatDelegate.prepare(writer).attackRanged(self, target)
}

class AttackThrow(private val target: Mob) : Action() {
    override fun invoke(self: Mob) = combatDelegate.prepare(writer).attackThrow(self, target)
}

class Look : Action() {
    override fun invoke(self: Mob) = lookDelegateProvider.prepare(writer).look(self, mobs)
}

class Doors : Action() {
    override fun invoke(self: Mob) = movementDelegateProvider.prepare(writer).doors(self)
}

class Move(private val direction: Direction) : Action() {
    override fun invoke(self: Mob) = movementDelegateProvider.prepare(writer).move(self, direction)
}

object Pass : Action() {
    override operator fun invoke(self: Mob) = Unit
}

class Retry(val errorMsg: String) : Action() {
    override operator fun invoke(self: Mob) = Unit
}

class Examine(private val item: String) : Action() {
    override fun invoke(self: Mob) = itemDelegateProvider.prepare(writer).examine(self, item)
}

class Inventory : Action() {
    override fun invoke(self: Mob) = itemDelegateProvider.prepare(writer).inventory(self)
}

