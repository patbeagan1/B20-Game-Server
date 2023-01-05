package com.pbeagan

import com.pbeagan.contextual.actions.type.Action
import com.pbeagan.contextual.actions.Inactive
import com.pbeagan.contextual.actions.Retry
import com.pbeagan.contextual.Mob
import com.pbeagan.data.reader.Reader
import java.util.Locale

class PlayerHandler(
    val commandParser: CommandParser
) {

    fun interpretPlayerAction(
        reader: Reader,
        mob: Mob,
    ): Action? {
        val read = reader.read(mob)
        val input = read?.lowercase(Locale.getDefault()) ?: return Inactive
        return commandParser(mob).map { it.first.toRegex() to it.second }
            .firstOrNull { it.first.matches(input) }
            ?.let { pair ->
                pair.first.find(input)
                    ?.groupValues
                    ?.also { println("Matches: $it") }
                    ?.let { pair.second.invoke(it) }
            } ?: Retry("Unknown Command")
    }

}