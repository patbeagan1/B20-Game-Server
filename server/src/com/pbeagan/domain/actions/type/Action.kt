package com.pbeagan.domain.actions.type

import com.pbeagan.domain.actions.Retry
import com.pbeagan.domain.attacks.AttackRanged
import com.pbeagan.domain.attacks.AttackThrow
import com.pbeagan.domain.attacks.Punch
import com.pbeagan.domain.types.AttackType.MAGIC
import com.pbeagan.domain.types.AttackType.MELEE
import com.pbeagan.domain.types.AttackType.RANGED
import com.pbeagan.domain.types.AttackType.THROWN
import com.pbeagan.domain.stats.AttackValue
import com.pbeagan.domain.Mob
import com.pbeagan.domain.getFirstVisibleMob
import com.pbeagan.domain.target
import com.pbeagan.data.writer.Writer

abstract class Action {
    abstract operator fun invoke(self: Mob)
    lateinit var writer: Writer

    fun damageResolution(target: Mob, damage: AttackValue) {
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

