package com.pbeagan.actions

import com.pbeagan.data.Mob
import com.pbeagan.roll6

class AttackThrow(private val target: Mob) : Action() {
    override fun invoke(self: Mob){
        val name = { m: Mob -> "${m.name}(${m.hearts} hp)" }
        writer.sayToRoomOf(target).combat("${name(self)} has throw attacked ${name(target)}!")
        if (roll6() + self.baseAtkThrow > target.armor) {
            damageResolution(target, self.baseAtkThrow)
        }
    }
}