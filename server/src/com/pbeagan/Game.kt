package com.pbeagan

import com.pbeagan.actions.Action
import com.pbeagan.actions.Follow
import com.pbeagan.actions.FreeAction
import com.pbeagan.actions.Inactive
import com.pbeagan.actions.Move
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
            writer.sayTo(it).info("Pending Action: ${it.action::class.java.simpleName}")
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
                    writer.sayTo(this).info("Pending Action: ${actionCurrent::class.java.simpleName}")
                    actionMove = actionCurrent
                    actionCurrent = null

                } else {
                    this.action = actionCurrent
                }
            }

            if (checkIfTurnContinues(actionCurrent)) {
                actionCurrent?.also { it.writer = writer }?.invoke(this)
            }
        }
    }

    private fun Mob.checkStartPlayerTurn(
        reader: Reader,
        writer: Writer
    ) {
        if (isPlayer && reader.isActive(this)) {
            if (action == Inactive) {
                writer.sayToAll().join("$name has joined the game!")
            }
            writer.sayToAll().turnStart(name)
        }
    }

    private fun Mob.degradeEffects(writer: Writer) {
        effects = effects.mapNotNull {
            it.roundsLeft -= 1
            if (it.roundsLeft <= 0) {
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
