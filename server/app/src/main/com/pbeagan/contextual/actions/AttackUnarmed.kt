package com.pbeagan.contextual.actions

import com.pbeagan.WorldState
import com.pbeagan.consolevision.TerminalColorStyle.Colors.Magenta
import com.pbeagan.consolevision.TerminalColorStyle.Colors.Red
import com.pbeagan.consolevision.TerminalColorStyle.style
import com.pbeagan.contextual.Mob
import com.pbeagan.contextual.actions.type.Action
import com.pbeagan.contextual.changeHealth
import com.pbeagan.contextual.formatHP
import dev.patbeagan.b20.domain.roll20
import dev.patbeagan.b20.domain.roll6
import dev.patbeagan.b20.domain.stats.AttackValue

class AttackUnarmed(private val target: Mob, val worldState: WorldState) : Action() {
    val range: Int = 1
    override operator fun invoke(self: Mob) = with(worldState) {
        if (target.currentRoom() != self.currentRoom()) return
        if (target.locationInRoom !in self.range(range)) return
        writer.sayToRoomOf(target)
            .combat("${self.formatHP()} has ${"Punched".style(Magenta)} ${target.formatHP()}!")
        val accuracy = self.baseAtkMelee.adjustBy(roll20())
        val dodge = target.dodge.adjustBy(roll20())
        if (accuracy > dodge) {
            val damage = roll6().value + self.strength.value - target.armor.value
            target.changeHealth(AttackValue(damage), worldState, writer)
        } else {
            writer.sayToRoomOf(target)
                .combat("${self.nameStyled} ${"missed".style(Red)} ${target.formatHP()}!")
        }
    }
}