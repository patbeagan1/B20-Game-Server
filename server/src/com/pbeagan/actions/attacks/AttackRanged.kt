package com.pbeagan.actions.attacks

import com.pbeagan.actions.Action
import com.pbeagan.data.Mob
import com.pbeagan.data.formatHP
import com.pbeagan.util.roll6

class AttackRanged(private val target: Mob) : Action() {
    override fun invoke(self: Mob) {
        writer.sayToRoomOf(target).combat("${self.formatHP()} has range attacked ${target.formatHP()}!")
        if (roll6() + self.baseAtkRanged > target.armor) {
            damageResolution(target, self.baseAtkRanged)
        }
    }
}