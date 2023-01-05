package com.pbeagan.domain.attacks

import com.pbeagan.domain.attacks.type.Attack
import com.pbeagan.stats.AttackValue
import com.pbeagan.domain.Mob
import com.pbeagan.domain.currentRoom
import com.pbeagan.domain.formatHP
import com.pbeagan.util.roll20
import com.pbeagan.util.roll6
import com.pbeagan.consolevision.TerminalColorStyle.Colors.Magenta
import com.pbeagan.consolevision.TerminalColorStyle.Colors.Red
import com.pbeagan.consolevision.TerminalColorStyle.style

class Punch(private val target: Mob) : Attack() {
    override val targetList: List<Mob> = listOf(target)
    override val range: Int = 1
    override operator fun invoke(self: Mob) {
        if (target.currentRoom() != self.currentRoom()) return
        if (target.locationInRoom !in self.range(range)) return
        writer.sayToRoomOf(target).combat("${self.formatHP()} has ${"Punched".style(Magenta)} ${target.formatHP()}!")
        val accuracy = self.baseAtkMelee.adjustBy(roll20())
        val dodge = target.dodge.adjustBy(roll20())
        if (accuracy > dodge) {
            val damage = roll6().value + self.strength.value - target.armor.value
            damageResolution(target, AttackValue(damage))
        } else {
            writer.sayToRoomOf(target).combat("${self.nameStyled} ${"missed".style(Red)} ${target.formatHP()}!")
        }
    }
}