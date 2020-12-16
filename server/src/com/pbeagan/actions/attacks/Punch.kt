package com.pbeagan.actions.attacks

import com.pbeagan.data.Mob
import com.pbeagan.data.currentRoom
import com.pbeagan.data.formatHP
import com.pbeagan.util.roll6

class Punch(private val target: Mob) : Attack() {
    override val targetList: List<Mob> = listOf(target)
    override val range: Int = 1
    override operator fun invoke(self: Mob) {
        if (target.currentRoom() != self.currentRoom()) return
        writer.sayToRoomOf(target).combat("${self.formatHP()} has melee attacked ${target.formatHP()}!")
        if (roll6() + self.baseAtkMelee > target.armor) {
            damageResolution(target, self.baseAtkMelee)
        }
    }
}