package com.pbeagan.actions

import com.pbeagan.data.Mob
import com.pbeagan.data.target
import kotlin.math.nextTowards

class Follow(val target: Mob) : Action() {
    override fun invoke(self: Mob) {
        val (targetX, targetY) = target.locationInRoom
        val (selfX, selfY) = self.locationInRoom

        val nextX = selfX.toDouble().nextTowards(targetX.toDouble())
        val nextY = selfY.toDouble().nextTowards(targetY.toDouble())

        self.locationInRoom = nextX.toInt() to nextY.toInt()
    }

    companion object {
        fun getOrRetry(mob: Mob, targetName: String): Action {
            val target = mob.target(targetName) ?: return Retry("That mob isn't here")
            return Follow(target)
        }
    }
}