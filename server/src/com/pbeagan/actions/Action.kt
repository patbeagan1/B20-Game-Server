package com.pbeagan.actions

import com.pbeagan.actions.attacks.AttackRanged
import com.pbeagan.actions.attacks.AttackThrow
import com.pbeagan.actions.attacks.Punch
import com.pbeagan.data.AttackType.MAGIC
import com.pbeagan.data.AttackType.MELEE
import com.pbeagan.data.AttackType.RANGED
import com.pbeagan.data.AttackType.THROWN
import com.pbeagan.data.CombatValue
import com.pbeagan.data.Mob
import com.pbeagan.data.getFirstVisibleMob
import com.pbeagan.data.target
import com.pbeagan.writer.Writer

abstract class Action {
    abstract operator fun invoke(self: Mob)
    lateinit var writer: Writer

    fun damageResolution(target: Mob, damage: CombatValue) {
        writer.sayToRoomOf(target).combat("${target.nameStyled} was hit for $damage damage!")
        target.hearts -= damage.value
        writer.sayToRoomOf(target).combat("${target.nameStyled} is down to ${target.hearts} hp!")
        if (target.hearts <= 0) {
            writer.sayToAll().dead("${target.nameStyled} has died.")
            target.die(writer)
        }
    }

    companion object {
        fun attackOrRetry(mob: Mob, target: String): Action {
            fun onTargetFound(target: Mob): Action = when (mob.preferredAttack) {
                MELEE -> Punch(target)
                RANGED -> AttackRanged(target)
                THROWN -> AttackThrow(target)
                MAGIC -> TODO()
            }
            return when {
                target.isEmpty() -> mob.getFirstVisibleMob()?.let { onTargetFound(it) }
                else -> mob.target(target)?.let { onTargetFound(it) }
            } ?: Retry("Looks like that mob isn't here...")
        }
    }
}

interface FreeAction

class MultiRoundActionDelegate(
    private val multiRoundAction: MultiRoundAction,
    private val totalDuration: Int = 1
) {

    private var currentDuration: Int = 0
    var isComplete = false

    fun invoke(self: Mob) = when {
        currentDuration == 0 -> multiRoundAction.onStart(self)
        currentDuration < totalDuration -> multiRoundAction.onDuring(self)
        else -> multiRoundAction.onComplete(self).also { isComplete = true }
    }.also { currentDuration += 1 }

    interface MultiRoundAction {
        val isComplete: Boolean
        fun onStart(self: Mob)
        fun onDuring(self: Mob)
        fun onComplete(self: Mob)
    }
}

