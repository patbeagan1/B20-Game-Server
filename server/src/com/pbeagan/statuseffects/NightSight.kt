package com.pbeagan.statuseffects

import com.pbeagan.domain.actions.type.Action
import com.pbeagan.domain.actions.type.MultiRoundActionDelegate
import com.pbeagan.domain.actions.type.MultiRoundActionDelegate.MultiRoundAction
import com.pbeagan.domain.Effect
import com.pbeagan.domain.EffectBase
import com.pbeagan.domain.EffectImpl
import com.pbeagan.domain.Mob
import com.pbeagan.stats.VisionValue
import com.pbeagan.consolevision.TerminalColorStyle.Colors.Magenta
import com.pbeagan.consolevision.TerminalColorStyle.style

class NightSight : Action(), MultiRoundAction {

    private val delegate = MultiRoundActionDelegate(this)
    override fun invoke(self: Mob): Unit = delegate.invoke(self)
    override val isComplete: Boolean get() = delegate.isComplete

    override fun onStart(self: Mob) = reportAttempt(self)
    override fun onDuring(self: Mob) = reportAttempt(self)

    private fun reportAttempt(self: Mob) {
        writer.sayToRoomOf(self).info("${self.nameStyled} is attempting to cast NightSight")
    }

    override fun onComplete(self: Mob) {
        self.addEffect(writer, NightSightEffect(self.nameStyled))
    }

    companion object {
        val spellName = "NightSight".style(colorForeground = Magenta)
    }

    class NightSightEffect(nameTarget: String) : EffectImpl(
        EffectBase(
            roundsLeft = 3,
            visionBright = VisionValue(-20),
            visionDim = VisionValue(10),
            visionDark = VisionValue(15),
            visionNone = VisionValue(2),
            descriptionActivation = "$nameTarget has been blessed with $spellName!",
            descriptionDeactivation = "$nameTarget's $spellName has worn off.",
            name = "NightSight",
            type = Effect.Type.SPELL
        )
    )
}