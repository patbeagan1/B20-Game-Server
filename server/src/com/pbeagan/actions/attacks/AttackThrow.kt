package com.pbeagan.actions.attacks

import com.pbeagan.actions.Action
import com.pbeagan.data.Mob
import com.pbeagan.data.formatHP
import com.pbeagan.roll6

class AttackThrow(private val target: Mob) : Action() {
    override fun invoke(self: Mob){
        writer.sayToRoomOf(target).combat("${self.formatHP()} has throw attacked ${target.formatHP()}!")
        if (roll6() + self.baseAtkThrow > target.armor) {
            damageResolution(target, self.baseAtkThrow)
        }
    }
}