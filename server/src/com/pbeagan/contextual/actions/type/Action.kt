package com.pbeagan.contextual.actions.type

import com.pbeagan.contextual.Mob
import com.pbeagan.contextual.actions.Retry
import com.pbeagan.contextual.attacks.AttackRanged
import com.pbeagan.contextual.attacks.AttackThrow
import com.pbeagan.contextual.attacks.Punch
import com.pbeagan.contextual.getFirstVisibleMob
import com.pbeagan.contextual.target
import com.pbeagan.data.writer.Writer
import dev.patbeagan.b20.domain.stats.AttackValue
import dev.patbeagan.b20.domain.types.AttackType.MAGIC
import dev.patbeagan.b20.domain.types.AttackType.MELEE
import dev.patbeagan.b20.domain.types.AttackType.RANGED
import dev.patbeagan.b20.domain.types.AttackType.THROWN

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

