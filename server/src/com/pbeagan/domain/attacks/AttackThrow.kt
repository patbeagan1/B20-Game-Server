package com.pbeagan.domain.attacks

import com.pbeagan.domain.attacks.type.Attack
import com.pbeagan.domain.Mob
import com.pbeagan.domain.formatHP
import com.pbeagan.util.roll6

class AttackThrow(private val target: Mob) : Attack() {
    override val targetList: List<Mob> = listOf(target)
    override val range: Int = 2
    override fun invoke(self: Mob) {
        writer.sayToRoomOf(target).combat("${self.formatHP()} has throw attacked ${target.formatHP()}!")
        if (self.baseAtkThrow.adjustBy(roll6()) > target.armor) {
            damageResolution(target, self.baseAtkThrow)
        }
    }
}