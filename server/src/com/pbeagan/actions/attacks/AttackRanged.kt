package com.pbeagan.actions.attacks

import com.pbeagan.data.Mob
import com.pbeagan.data.formatHP
import com.pbeagan.util.roll6

class AttackRanged(private val target: Mob) : Attack() {
    override val targetList: List<Mob> = listOf(target)
    override val range: Int = 2
    override fun invoke(self: Mob) {
        writer.sayToRoomOf(target).combat("${self.formatHP()} has range attacked ${target.formatHP()}!")
        if (roll6() + self.baseAtkRanged.value > target.armor) {
            damageResolution(target, self.baseAtkRanged)
        }
    }
}