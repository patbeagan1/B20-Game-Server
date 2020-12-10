package com.pbeagan

import com.pbeagan.actions.Action
import com.pbeagan.actions.FreeAction
import com.pbeagan.actions.Inactive
import com.pbeagan.actions.Repeat
import com.pbeagan.data.MobBehavior
import com.pbeagan.writer.Reader
import com.pbeagan.writer.Writer
import mobs

class Game {

    val playerHandler = PlayerHandler()

    fun gameLoop(writer: Writer, reader: Reader) {
        mobs.sortedByDescending { roll20() + it.awareness }.forEach { mob ->

            mob.effects = mob.effects.mapNotNull {
                it.roundsLeft -= 1
                if (it.roundsLeft <= 0) {
                    writer.sayToRoomOf(mob).info(it.descriptionDeactivation)
                    null
                } else {
                    it
                }
            }

            if (mob.isPlayer && reader.isActive(mob)) {
                if (mob.action == Inactive) {
                    writer.sayToAll().join("${mob.name} has joined the game!")
                }
                writer.sayToAll().turnStart(mob.name)
            }

            var action: Action? = null
            while (checkIfTurnContinues(action)) {

                action = if (mob.isPlayer) {
                    playerHandler.interpretPlayerAction(reader, mob)
                } else {
                    mob.getAction(reader)
                }
                if (action != null) {
                    mob.action = action
                }
                if (checkIfTurnContinues(action)) {
                    action?.also { it.writer = writer }?.invoke(mob)
                }
            }

            writer.sayTo(mob).info("Pending Action: ${mob.action::class.java.simpleName}")
        }
        mobs.forEach { it.action.apply { this.writer = writer }(it) }
    }

    private fun checkIfTurnContinues(action: Action?) =
        action == null || action is FreeAction || action is Repeat && action.isRepeatFreeAction
}
