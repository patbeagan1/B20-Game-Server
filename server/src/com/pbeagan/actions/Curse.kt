package com.pbeagan.actions

import com.pbeagan.data.Effect
import com.pbeagan.data.EffectImpl
import com.pbeagan.data.Mob
import com.pbeagan.data.target

class Curse(private val target: Mob) : Action() {
    override fun invoke(self: Mob) {
        val effect = EffectImpl(
            roundsLeft = 1,
            totalHearts = -10,
            descriptionActivation = "${target.name} has been cursed by ${self.name}!",
            descriptionDeactivation = "The curse on ${target.name} has worn off.",
            name = "Curse",
            type = Effect.Type.SPELL
        )
        target.addEffect(writer, effect)
    }

    companion object {
        fun getOrRetry(mob: Mob, targetName: String): Action {
            val target = mob.target(targetName) ?: return Retry("That mob isn't here")
            return Curse(target)
        }
    }
}