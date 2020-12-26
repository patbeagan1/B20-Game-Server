package com.pbeagan

import com.pbeagan.actions.Action
import com.pbeagan.actions.Follow
import com.pbeagan.actions.FreeAction
import com.pbeagan.actions.Inactive
import com.pbeagan.actions.Move
import com.pbeagan.actions.MultiRoundActionDelegate.MultiRoundAction
import com.pbeagan.actions.Pass
import com.pbeagan.actions.Repeat
import com.pbeagan.data.Mob
import com.pbeagan.util.roll20
import com.pbeagan.writer.Reader
import com.pbeagan.writer.Writer
import mobs

class Game {

    private val playerHandler = PlayerHandler()

    fun gameLoop(writer: Writer, reader: Reader) {
        mobs.sortedByDescending { roll20() + it.awareness }.forEach {
            it.run {
                degradeEffects(writer)
                checkStartPlayerTurn(reader, writer)
                takeTurn(reader, writer)
            }
            writer.sayTo(it).pending(it.action)
        }
        mobs.forEach {
            it.actionMove?.apply { this.writer = writer }?.invoke(it)
            it.action.apply { this.writer = writer }(it)
        }
    }

    private fun Mob.takeTurn(reader: Reader, writer: Writer) {
        var actionCurrent: Action? = null
        actionMove = null
        while (checkIfTurnContinues(actionCurrent)) {

            if (this.action is Follow) {
                break
            }

            actionCurrent = if (isPlayer) {
                playerHandler.interpretPlayerAction(reader, this)
            } else {
                getAction(reader)
            }

            if (actionCurrent != null) {
                if (actionCurrent is Move && actionMove == null) {
                    // we get one move action for free, so
                    // we can forward it into actionMove and act like nothing happened.
                    writer.sayTo(this).pending(actionCurrent)
                    actionMove = actionCurrent
                    actionCurrent = null
                } else {
                    clearExpiredMultiRoundAction()
                    if (this.action is MultiRoundAction) {
                        // Breaking here means that we can end the turn and continue the action.
                        if (actionCurrent == Pass) break
                        // If we are not passing, we need to make sure they know that the action is ongoing.
                        if (verifyMultiRoundActionShouldStop(writer, actionCurrent, reader)) {
                            // If the action is stopped, we set the new action here.
                            this.action = actionCurrent
                        } else {
                            // If the action is not stopped, we loop again for a chance to correct.
                            actionCurrent = null
                            writer.sayTo(this).error("What would you like to do instead?")
                        }
                    } else {
                        this.action = actionCurrent
                    }
                }
            }

            // This executes free actions. This check will happen again at the start of the loop.
            if (checkIfTurnContinues(actionCurrent)) {
                actionCurrent?.also { it.writer = writer }?.invoke(this)
            }
        }
    }

    private fun Mob.clearExpiredMultiRoundAction() {
        (this.action as? MultiRoundAction)
            ?.takeIf { it.isComplete }
            ?.let { this.action = Pass }
    }

    private fun Mob.verifyMultiRoundActionShouldStop(
        writer: Writer,
        actionCurrent: Action,
        reader: Reader
    ): Boolean {
        writer.sayTo(this).error(
            """You are currently performing ${this.action::class.java.simpleName}. 
Would you like to stop doing that and start doing ${actionCurrent::class.java.simpleName} instead?
Note, you can pass your turn to continue the action."""
        )
        val read = reader.read(this)
        return read?.matches("[Yy](es)*".toRegex()) ?: false
    }

    private fun Mob.checkStartPlayerTurn(
        reader: Reader,
        writer: Writer
    ) {
        if (isPlayer && reader.isActive(this)) {
            if (action == Inactive) {
                writer.sayToAll().join("$nameStyled has joined the game!")
            }
            writer.sayToAll().turnStart(nameStyled)
        }
    }

    private fun Mob.degradeEffects(writer: Writer) {
        effects = effects.mapNotNull {
            it.roundsLeft -= 1
            if (it.roundsLeft < 0) {
                writer.sayToRoomOf(this).info(it.descriptionDeactivation)
                null
            } else {
                it
            }
        }
    }

    private fun checkIfTurnContinues(action: Action?) =
        action == null || action is FreeAction || action is Repeat && action.isRepeatFreeAction
}
