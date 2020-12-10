package com.pbeagan.actions

import com.pbeagan.data.Mob
import com.pbeagan.roll6

class AttackRanged(private val target: Mob) : Action() {
    override fun invoke(self: Mob) {
        val name = { m: Mob -> "${m.name}(${m.hearts} hp)" }
        writer.sayToRoomOf(target).combat("${name(self)} has range attacked ${name(target)}!")
        if (roll6() + self.baseAtkRanged > target.armor) {
            damageResolution(target, self.baseAtkRanged)
        }
    }
}