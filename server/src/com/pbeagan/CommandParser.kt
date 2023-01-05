package com.pbeagan


import com.pbeagan.domain.actions.*
import com.pbeagan.domain.types.Direction
import com.pbeagan.domain.Mob
import com.pbeagan.domain.actions.type.Action
import com.pbeagan.statuseffects.Curse
import com.pbeagan.statuseffects.NightSight
import com.pbeagan.util.safeLet

class CommandParser {

    operator fun invoke(mob: Mob): List<Pair<String, (List<String>) -> Action>> = listOf(
        // Util
        "(\\.|\n|again)" to { Repeat(mob.action) },
        "debug$ARG" to { i ->
            safeLet(i.getOrNull(1)) { target ->
                Debug(target)
            } ?: Retry("Debug what?")
        },
        "(wait|pass|end| )" to { Pass },

        // INFO
        "l(s|l|ook)?" to { Look() },
        "do(or(s)?)?" to { Doors() },
        "exit(s)?" to { Doors() },
        "m(ap)?" to { _ -> MapLocal() },
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
                com.pbeagan.domain.actions.Consume.getOrRetry(mob, itemName)
            } ?: Retry("What would you like to consume?")
        },
        "(give)$ARG$ARG" to { i ->
            safeLet(i.getOrNull(2), i.getOrNull(3)) { target, itemName ->
                Give.getOrRetry(mob, target, itemName)
            } ?: Retry("What would you like to give?")
        },

        // Movement
        "n(orth)?" to { _ -> Move.getOrRetry(mob, listOf(Direction.NORTH)) },
        "s(outh)?" to { _ -> Move.getOrRetry(mob, listOf(Direction.SOUTH)) },
        "e(ast)?" to { _ -> Move.getOrRetry(mob, listOf(Direction.EAST)) },
        "w(est)?" to { _ -> Move.getOrRetry(mob, listOf(Direction.WEST)) },
        "u(p)?" to { _ -> Move.getOrRetry(mob, listOf(Direction.UP)) },
        "d(own)?" to { _ -> Move.getOrRetry(mob, listOf(Direction.DOWN)) },
        // moving multiple paces at a time
        "([nsewud]+)" to { i ->
            val directionList = i.getOrNull(1)?.take(4)?.map { input ->
                Direction.values().first {
                    it.name.startsWith(input, true)
                }
            }
            if (directionList == null) {
                Retry("Sorry, couldn't parse that.")
            } else {
                Move.getOrRetry(mob, directionList)
            }
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

        "cast$ARG" to { i ->
            safeLet(i.getOrNull(1)) {
                when (it) {
                    "nightsight" -> NightSight()
                    else -> null
                }
            } ?: Retry("I don't know that spell")
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