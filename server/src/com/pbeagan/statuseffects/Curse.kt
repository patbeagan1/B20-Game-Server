package com.pbeagan.statuseffects

import com.pbeagan.domain.actions.type.Action
import com.pbeagan.domain.actions.type.MultiRoundActionDelegate
import com.pbeagan.domain.actions.type.MultiRoundActionDelegate.MultiRoundAction
import com.pbeagan.domain.actions.Retry
import com.pbeagan.domain.Effect
import com.pbeagan.domain.EffectBase
import com.pbeagan.domain.EffectImpl
import com.pbeagan.stats.HealthValue
import com.pbeagan.domain.Mob
import com.pbeagan.domain.target

class Curse(private val target: Mob) : Action(), MultiRoundAction {

    private val delegate = MultiRoundActionDelegate(this)
    override fun invoke(self: Mob): Unit = delegate.invoke(self)
    override val isComplete: Boolean get() = delegate.isComplete

    override fun onStart(self: Mob) {
        writer.sayToRoomOf(self).combat("${self.nameStyled} is attempting to Curse ${target.nameStyled}")
    }

    override fun onDuring(self: Mob) {
        writer.sayToRoomOf(self).combat("${self.nameStyled} is attempting to Curse ${target.nameStyled}")
    }

    override fun onComplete(self: Mob) {
        target.addEffect(writer,
            CurseEffect(target.nameStyled, self.nameStyled)
        )
    }

    companion object {
        fun getOrRetry(mob: Mob, targetName: String): Action {
            val target = mob.target(targetName) ?: return Retry("That mob isn't here")
            return Curse(target)
        }
    }

    class CurseEffect(nameTarget: String, nameCaster: String) : EffectImpl(
        EffectBase(
            roundsLeft = 1,
            totalHearts = HealthValue(-10),
            descriptionActivation = "$nameTarget has been cursed by $nameCaster!",
            descriptionDeactivation = "The curse on $nameTarget has worn off.",
            name = "Curse",
            type = Effect.Type.SPELL
        )
    )
}