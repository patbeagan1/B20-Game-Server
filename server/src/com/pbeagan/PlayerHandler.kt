package com.pbeagan

import com.pbeagan.domain.actions.type.Action
import com.pbeagan.domain.actions.Inactive
import com.pbeagan.domain.actions.Retry
import com.pbeagan.domain.Mob
import com.pbeagan.writer.Reader
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