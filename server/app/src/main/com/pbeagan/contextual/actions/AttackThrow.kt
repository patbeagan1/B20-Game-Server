package com.pbeagan.contextual.actions

import com.pbeagan.WorldState
import com.pbeagan.contextual.Mob
import com.pbeagan.contextual.actions.type.Action
import com.pbeagan.contextual.changeHealth
import com.pbeagan.contextual.formatHP
import dev.patbeagan.b20.domain.roll6

class AttackThrow(private val target: Mob, val worldState: WorldState) : Action() {
    override fun invoke(self: Mob) {
        writer.sayToRoomOf(target)
            .combat("${self.formatHP()} has throw attacked ${target.formatHP()}!")
        if (self.baseAtkThrow.adjustBy(roll6()) > target.armor) {
            target.changeHealth(self.baseAtkThrow, worldState, writer)
        }
    }
}