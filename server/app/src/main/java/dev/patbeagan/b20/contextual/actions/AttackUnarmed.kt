package dev.patbeagan.b20.contextual.actions

import dev.patbeagan.b20.contextual.Mob
import com.pbeagan.contextual.actions.type.Action
import com.pbeagan.contextual.changeHealth
import com.pbeagan.contextual.formatHP
import dev.patbeagan.b20.WorldState
import dev.patbeagan.b20.domain.roll20
import dev.patbeagan.b20.domain.roll6
import dev.patbeagan.b20.domain.stats.AttackValue
import dev.patbeagan.consolevision.ansi.AnsiColor
import dev.patbeagan.consolevision.ansi.style

class AttackUnarmed(private val target: Mob, val worldState: WorldState) : Action() {
    val range: Int = 1
    override operator fun invoke(self: Mob) = with(worldState) {
        if (target.currentRoom() != self.currentRoom()) return
        if (target.locationInRoom !in self.range(range)) return
        writer.sayToRoomOf(target)
            .combat("${self.formatHP()} has ${"Punched".style(AnsiColor.Magenta)} ${target.formatHP()}!")
        val accuracy = self.baseAtkMelee.adjustBy(roll20())
        val dodge = target.dodge.adjustBy(roll20())
        if (accuracy > dodge) {
            val damage = roll6().value + self.strength.value - target.armor.value
            target.changeHealth(AttackValue(damage), worldState, writer)
        } else {
            writer.sayToRoomOf(target)
                .combat("${self.nameStyled} ${"missed".style(AnsiColor.Red)} ${target.formatHP()}!")
        }
    }
}