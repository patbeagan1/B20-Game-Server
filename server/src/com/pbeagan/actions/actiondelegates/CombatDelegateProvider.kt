package com.pbeagan.actions.actiondelegates

import com.pbeagan.actions.actiondelegates.CombatDelegateProvider.CombatDelegate
import com.pbeagan.mob.Mob
import com.pbeagan.mob.currentRoom
import com.pbeagan.roll6

class CombatDelegateProvider : ActionDelegateProvider<CombatDelegate>() {
    interface CombatDelegate {
        fun damageResolution(target: Mob, damage: Int)
        fun attackMelee(self: Mob, target: Mob)
        fun attackRanged(self: Mob, target: Mob)
        fun attackThrow(self: Mob, target: Mob)
    }

    private inner class CombatDelegateImpl : CombatDelegate {
        override fun damageResolution(target: Mob, damage: Int) {
            writer.sayToRoomOf(target).combat("${target.name} was hit for $damage damage!")
            target.hearts -= damage
            writer.sayToRoomOf(target).combat("${target.name} is down to ${target.hearts} hp!")
            if (target.hearts < 0) {
                writer.sayToAll().dead("${target.name} has died.")
            }
        }

        override fun attackMelee(self: Mob, target: Mob) {
            if (target.currentRoom() != self.currentRoom()) return
            val name = { m: Mob -> "${m.name}(${m.hearts} hp)" }
            writer.sayToRoomOf(target).combat("${name(self)} has attacked ${name(target)}!")
            if (roll6() + self.baseAtkMelee > target.armor) {
                damageResolution(target, self.baseAtkMelee)
            }
        }

        override fun attackRanged(self: Mob, target: Mob) {
            if (roll6() + self.baseAtkRanged > target.armor) {
                damageResolution(target, self.baseAtkRanged)
            }
        }

        override fun attackThrow(self: Mob, target: Mob) {
            if (roll6() + self.baseAtkThrow > target.armor) {
                damageResolution(target, self.baseAtkThrow)
            }
        }
    }

    override fun build(): CombatDelegate = CombatDelegateImpl()
}
