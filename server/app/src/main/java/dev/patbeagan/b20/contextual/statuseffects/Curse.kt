package com.pbeagan.contextual.statuseffects

import dev.patbeagan.b20.contextual.Mob
import com.pbeagan.contextual.actions.Retry
import com.pbeagan.contextual.actions.type.Action
import com.pbeagan.contextual.actions.type.MultiRoundActionDelegate
import com.pbeagan.contextual.actions.type.MultiRoundActionDelegate.MultiRoundAction
import dev.patbeagan.b20.WorldState
import dev.patbeagan.b20.domain.Effect
import dev.patbeagan.b20.domain.EffectBase
import dev.patbeagan.b20.domain.EffectImpl
import dev.patbeagan.b20.domain.stats.HealthValue

class Curse(private val target: Mob) : Action(), MultiRoundAction {

    private val delegate = MultiRoundActionDelegate(this)
    override fun invoke(self: Mob): Unit = delegate.invoke(self)
    override val isComplete: Boolean get() = delegate.isComplete

    override fun onStart(self: Mob) {
        writer.sayToRoomOf(self)
            .combat("${self.nameStyled} is attempting to Curse ${target.nameStyled}")
    }

    override fun onDuring(self: Mob) {
        writer.sayToRoomOf(self)
            .combat("${self.nameStyled} is attempting to Curse ${target.nameStyled}")
    }

    override fun onComplete(self: Mob) {
        target.addEffect(
            writer,
            CurseEffect(target.nameStyled, self.nameStyled)
        )
    }

    companion object {
        fun getOrRetry(mob: Mob, targetName: String, worldState: WorldState): Action = with(worldState) {
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
