package dev.patbeagan.b20.contextual.statuseffects

import dev.patbeagan.b20.contextual.Mob
import com.pbeagan.contextual.actions.type.Action
import com.pbeagan.contextual.actions.type.MultiRoundActionDelegate
import com.pbeagan.contextual.actions.type.MultiRoundActionDelegate.MultiRoundAction
import dev.patbeagan.b20.domain.Effect
import dev.patbeagan.b20.domain.EffectBase
import dev.patbeagan.b20.domain.EffectImpl
import dev.patbeagan.b20.domain.stats.VisionValue
import dev.patbeagan.consolevision.ansi.AnsiColor
import dev.patbeagan.consolevision.style

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
        val spellName = "NightSight".style(colorForeground = AnsiColor.Magenta)
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
