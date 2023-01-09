package com.pbeagan.contextual.actions

import dev.patbeagan.b20.contextual.Mob
import com.pbeagan.contextual.actions.type.Action
import com.pbeagan.contextual.changeHealth
import com.pbeagan.contextual.formatHP
import dev.patbeagan.b20.WorldState
import dev.patbeagan.b20.domain.roll6

class AttackRanged(private val target: Mob, val worldState: WorldState) : Action() {
    override fun invoke(self: Mob) {
        writer.sayToRoomOf(target)
            .combat("${self.formatHP()} has range attacked ${target.formatHP()}!")
        if (self.baseAtkRanged.adjustBy(roll6()) > target.armor) {
            target.changeHealth(self.baseAtkRanged, worldState, writer)
        }
    }
}