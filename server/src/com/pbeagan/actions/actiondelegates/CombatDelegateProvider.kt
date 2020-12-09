package com.pbeagan.actions.actiondelegates

import com.pbeagan.actions.actiondelegates.CombatDelegateProvider.CombatDelegate
import com.pbeagan.data.Mob
import com.pbeagan.data.currentRoom
import com.pbeagan.roll6

class CombatDelegateProvider : ActionDelegateProvider<CombatDelegate>() {
    interface CombatDelegate {
        fun attackMelee(self: Mob, target: Mob)
        fun attackRanged(self: Mob, target: Mob)
        fun attackThrow(self: Mob, target: Mob)
    }

    private inner class CombatDelegateImpl : CombatDelegate {
        override fun attackMelee(self: Mob, target: Mob) {
            if (target.currentRoom() != self.currentRoom()) return
            val name = { m: Mob -> "${m.name}(${m.hearts} hp)" }
            writer.sayToRoomOf(target).combat("${name(self)} has melee attacked ${name(target)}!")
            if (roll6() + self.baseAtkMelee > target.armor) {
                damageResolution(target, self.baseAtkMelee)
            }
        }

        override fun attackRanged(self: Mob, target: Mob) {
            val name = { m: Mob -> "${m.name}(${m.hearts} hp)" }
            writer.sayToRoomOf(target).combat("${name(self)} has range attacked ${name(target)}!")
            if (roll6() + self.baseAtkRanged > target.armor) {
                damageResolution(target, self.baseAtkRanged)
            }
        }

        override fun attackThrow(self: Mob, target: Mob) {
            val name = { m: Mob -> "${m.name}(${m.hearts} hp)" }
            writer.sayToRoomOf(target).combat("${name(self)} has throw attacked ${name(target)}!")
            if (roll6() + self.baseAtkThrow > target.armor) {
                damageResolution(target, self.baseAtkThrow)
            }
        }
    }

    override fun build(): CombatDelegate = CombatDelegateImpl()
}
