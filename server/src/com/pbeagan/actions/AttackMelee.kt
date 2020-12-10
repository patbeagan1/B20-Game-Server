package com.pbeagan.actions

import com.pbeagan.data.Mob
import com.pbeagan.data.currentRoom
import com.pbeagan.roll6

class AttackMelee(private val target: Mob) : Action() {
    override operator fun invoke(self: Mob) {
        if (target.currentRoom() != self.currentRoom()) return
        val name = { m: Mob -> "${m.name}(${m.hearts} hp)" }
        writer.sayToRoomOf(target).combat("${name(self)} has melee attacked ${name(target)}!")
        if (roll6() + self.baseAtkMelee > target.armor) {
            damageResolution(target, self.baseAtkMelee)
        }
    }
}