package com.pbeagan

import com.pbeagan.actions.Action
import com.pbeagan.actions.Consume
import com.pbeagan.actions.Curse
import com.pbeagan.actions.Debug
import com.pbeagan.actions.Doors
import com.pbeagan.actions.Drop
import com.pbeagan.actions.Examine
import com.pbeagan.actions.Follow
import com.pbeagan.actions.Give
import com.pbeagan.actions.Inactive
import com.pbeagan.actions.Inventory
import com.pbeagan.actions.LocalMap
import com.pbeagan.actions.Look
import com.pbeagan.actions.Move
import com.pbeagan.actions.Pass
import com.pbeagan.actions.Repeat
import com.pbeagan.actions.Retry
import com.pbeagan.actions.Settings
import com.pbeagan.actions.Take
import com.pbeagan.data.Direction
import com.pbeagan.data.Mob
import com.pbeagan.util.safeLet
import com.pbeagan.writer.Reader

class PlayerHandler {

    fun interpretPlayerAction(
        reader: Reader,
        mob: Mob
    ): Action? {
        val read = reader.read(mob)
        val input = read?.toLowerCase() ?: return Inactive
        return getCommands(mob).map { it.first.toRegex() to it.second }
            .firstOrNull { it.first.matches(input) }
            ?.let { pair ->
                pair.first.find(input)
                    ?.groupValues
                    ?.also { println("Matches: $it") }
                    ?.let { pair.second.invoke(it) }
            } ?: Retry("Unknown Command")
    }

    private fun getCommands(mob: Mob) = listOf<Pair<String, ((List<String>) -> Action)>>(
        // Util
        "(\\.|\n|again)" to { _ -> Repeat(mob.action) },
        "debug$ARG" to { i ->
            safeLet(i.getOrNull(1)) { target ->
                Debug(target)
            } ?: Retry("Debug what?")
        },
        "(wait|pass)" to { _ -> Pass },

        // INFO
        "l(s|l|ook)?" to { _ -> Look() },
        "do(or(s)?)?" to { _ -> Doors() },
        "exit(s)?" to { _ -> Doors() },
        "m(ap)?" to { _ -> LocalMap() },
        "ex(amine)?$ARG" to { i ->
            safeLet(i.getOrNull(2)) { target ->
                Examine(target)
            } ?: Retry("What should I examine?")
        },

        // Items
        "i(nventory)?" to { _ -> Inventory() },
        "take$ARG" to { i ->
            safeLet(i.getOrNull(1)) { itemName ->
                if (itemName.isBlank()) {
                    Retry("What would you like to take?")
                } else {
                    Take.getOrRetry(mob, itemName)
                }
            } ?: Retry("What would you like to take?")
        },
        "drop$ARG" to { i ->
            safeLet(i.getOrNull(1)) { itemName ->
                if (itemName.isBlank()) {
                    Retry("What would you like to drop?")
                } else {
                    Drop.getOrRetry(mob, itemName)
                }
            } ?: Retry("What would you like to drop?")
        },
        "(eat|consume|quaff)$ARG" to { i ->
            safeLet(i.getOrNull(2)) { itemName ->
                Consume.getOrRetry(mob, itemName)
            } ?: Retry("What would you like to consume?")
        },
        "(give)$ARG$ARG" to { i ->
            safeLet(i.getOrNull(2), i.getOrNull(3)) { target, itemName ->
                Give.getOrRetry(mob, target, itemName)
            } ?: Retry("What would you like to give?")
        },

        // Movement
        "n(orth)?" to { _ -> Move.getOrRetry(mob, Direction.NORTH) },
        "s(outh)?" to { _ -> Move.getOrRetry(mob, Direction.SOUTH) },
        "e(ast)?" to { _ -> Move.getOrRetry(mob, Direction.EAST) },
        "w(est)?" to { _ -> Move.getOrRetry(mob, Direction.WEST) },
        "u(p)?" to { _ -> Move.getOrRetry(mob, Direction.UP) },
        "d(own)?" to { _ -> Move.getOrRetry(mob, Direction.DOWN) },
        "follow$ARG" to { i ->
            safeLet(i.getOrNull(1)) { targetName ->
                Follow.getOrRetry(mob, targetName)
            } ?: Retry("Who would you like to follow?")
        },

        // Combat
        "(atk|attack)$ARG" to { i ->
            safeLet(i.getOrNull(2)) { mobName ->
                Action.attackOrRetry(mob, mobName)
            } ?: Retry("You need to specify a target!")
        },
        "(curse)$ARG" to { i ->
            safeLet(i.getOrNull(2)) { mobName ->
                Curse.getOrRetry(mob, mobName)
            } ?: Retry("You need to specify a target!")
        },

        // Settings
        "set$ARG$ARG" to { i ->
            val action: Action = safeLet(
                i.getOrNull(1),
                i.getOrNull(2)
            ) { settingKey, settingValue ->
                when (settingKey) {
                    "attack" -> Settings.getAttack(settingValue)
                    else -> Retry("I don't know about that setting.")
                }
            } ?: Retry("What would you like to set?")
            action
        }

        // follow
        // steal
        // scan

    )

    companion object {
        private const val ARG = " *([^\\s]*)?"
    }
}