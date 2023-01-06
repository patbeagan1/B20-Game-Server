package com.pbeagan.contextual.actions.type

import com.pbeagan.contextual.Mob

class MultiRoundActionDelegate(
    private val multiRoundAction: MultiRoundAction,
    private val totalDuration: Int = 1
) {

    private var currentDuration: Int = 0
    var isComplete = false

    fun invoke(self: Mob) = when {
        currentDuration == 0 -> multiRoundAction.onStart(self)
        currentDuration < totalDuration -> multiRoundAction.onDuring(self)
        else -> multiRoundAction.onComplete(self).also { isComplete = true }
    }.also { currentDuration += 1 }

    interface MultiRoundAction {
        val isComplete: Boolean
        fun onStart(self: Mob)
        fun onDuring(self: Mob)
        fun onComplete(self: Mob)
    }
}