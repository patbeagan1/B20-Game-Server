package com.pbeagan.actions.attacks

import com.pbeagan.data.Mob
import com.pbeagan.data.currentRoom
import com.pbeagan.data.formatHP
import com.pbeagan.util.roll20
import com.pbeagan.util.roll6
import com.pbeagan.writer.TerminalColorStyle.Colors.Magenta
import com.pbeagan.writer.TerminalColorStyle.Colors.Red
import com.pbeagan.writer.TerminalColorStyle.style

class Punch(private val target: Mob) : Attack() {
    override val targetList: List<Mob> = listOf(target)
    override val range: Int = 1
    override operator fun invoke(self: Mob) {
        if (target.currentRoom() != self.currentRoom()) return
        if (target.locationInRoom !in self.range(range)) return
        writer.sayToRoomOf(target).combat("${self.formatHP()} has ${"Punched".style(Magenta)} ${target.formatHP()}!")
        val accuracy = roll20() + self.baseAtkMelee
        val dodge = roll20() + target.dodge
        if (accuracy > dodge) {
            val damage = roll6() + self.strength - target.armor
            damageResolution(target, damage)
        } else {
            writer.sayToRoomOf(target).combat("${self.nameStyled} ${"missed".style(Red)} ${target.formatHP()}!")
        }
    }
}