package com.pbeagan.contextual.attacks

import com.pbeagan.WorldState
import com.pbeagan.contextual.Mob
import com.pbeagan.contextual.attacks.type.Attack
import com.pbeagan.contextual.formatHP
import dev.patbeagan.b20.domain.roll6

class AttackRanged(private val target: Mob, val worldState: WorldState) : Attack() {
    override val targetList: List<Mob> = listOf(target)
    override val range: Int = 2
    override fun invoke(self: Mob) {
        writer.sayToRoomOf(target).combat("${self.formatHP()} has range attacked ${target.formatHP()}!")
        if (self.baseAtkRanged.adjustBy(roll6()) > target.armor) {
            damageResolution(target, self.baseAtkRanged, worldState)
        }
    }
}