package com.pbeagan.actions

import com.pbeagan.data.AttackType.MAGIC
import com.pbeagan.data.AttackType.MELEE
import com.pbeagan.data.AttackType.RANGED
import com.pbeagan.data.AttackType.THROWN
import com.pbeagan.data.Mob
import com.pbeagan.data.getFirstVisibleMob
import com.pbeagan.data.target
import com.pbeagan.writer.Writer

abstract class Action {
    abstract operator fun invoke(self: Mob)
    lateinit var writer: Writer

    fun damageResolution(target: Mob, damage: Int) {
        writer.sayToRoomOf(target).combat("${target.name} was hit for $damage damage!")
        target.hearts -= damage
        writer.sayToRoomOf(target).combat("${target.name} is down to ${target.hearts} hp!")
        if (target.hearts < 0) {
            writer.sayToAll().dead("${target.name} has died.")
        }
    }

    companion object {
        fun attackOrRetry(mob: Mob, target: String): Action {
            fun onTargetFound(target: Mob): Action = when (mob.preferredAttack) {
                MELEE -> AttackMelee(target)
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

