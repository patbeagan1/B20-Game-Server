package com.pbeagan.statuseffects

import com.pbeagan.actions.Action
import com.pbeagan.actions.MultiRoundActionDelegate
import com.pbeagan.actions.MultiRoundActionDelegate.MultiRoundAction
import com.pbeagan.actions.Retry
import com.pbeagan.data.Effect
import com.pbeagan.data.EffectBase
import com.pbeagan.data.EffectImpl
import com.pbeagan.data.HealthValue
import com.pbeagan.data.Mob
import com.pbeagan.data.target

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
