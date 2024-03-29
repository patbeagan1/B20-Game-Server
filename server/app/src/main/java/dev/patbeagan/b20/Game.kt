package dev.patbeagan.b20

import dev.patbeagan.b20.contextual.Mob
import com.pbeagan.contextual.actions.Follow
import com.pbeagan.contextual.actions.Inactive
import dev.patbeagan.b20.contextual.actions.Move
import com.pbeagan.contextual.actions.Pass
import com.pbeagan.contextual.actions.Repeat
import com.pbeagan.contextual.actions.type.Action
import com.pbeagan.contextual.actions.type.FreeAction
import com.pbeagan.contextual.actions.type.MultiRoundActionDelegate.MultiRoundAction
import com.pbeagan.data.reader.Reader
import com.pbeagan.data.writer.Writer
import dev.patbeagan.b20.domain.roll20

class Game(private val worldState: WorldState) {

    val mobs get() = worldState.mobs
    private val playerHandler = PlayerHandler()

    fun gameLoop(writer: Writer, reader: Reader) {
        mobs.sortedByDescending { roll20() + it.awareness.value }.forEach {
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
                playerHandler.interpretPlayerAction(reader, this, worldState)
            } else {
                getAction(reader, worldState)
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
                        if (actionCurrent == Pass || actionCurrent is FreeAction) break
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
        reader: Reader,
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
        writer: Writer,
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
