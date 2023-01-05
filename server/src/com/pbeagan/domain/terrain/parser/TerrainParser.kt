package com.pbeagan.domain.terrain.parser

import com.pbeagan.consolevision.List2D
import com.pbeagan.consolevision.TerminalColorStyle
import com.pbeagan.domain.terrain.Grass
import com.pbeagan.domain.terrain.Pavement
import com.pbeagan.domain.terrain.Wall
import com.pbeagan.domain.terrain.Water

object TerrainParser {
    fun parse(s: String) = s
        .split(';', '\n')
        .also { list ->
            // making sure that this is a rectangle - all rows same width
            val size = list.map { it.length }.distinct().size
            if (size != 1) {
                throw IndexOutOfBoundsException("The given room is not rectangular.\n$s")
            }
        }
        .reversed() // we want north to be at the top
        .map { rows ->
            rows.toCharArray().map {
                when (it) {
                    Grass.SYMBOL -> Grass(
                        arrayOf("\"", "\"", "\"", "\"", "'", "*"),
                        TerminalColorStyle.TerminalStyle(
                            TerminalColorStyle.Colors.Custom(34, 139, 34).mutate(50),
                            TerminalColorStyle.Colors.Custom(34, 110, 34),
                            TerminalColorStyle.SGR.Bold
                        )
                    )
                    Pavement.SYMBOL -> Pavement(
                        ".",
                        TerminalColorStyle.TerminalStyle(
                            TerminalColorStyle.Colors.Black1,
                            TerminalColorStyle.Colors.Black2
                        )
                    )
                    Wall.SYMBOL -> Wall(
                        TerminalColorStyle.DOTS_HIGH.toString(),
                        TerminalColorStyle.TerminalStyle(
                            TerminalColorStyle.Colors.Black,
                            TerminalColorStyle.Colors.Black1
                        )
                    )
                    Water.SYMBOL -> Water(
                        "⍨",
                        TerminalColorStyle.TerminalStyle(
                            TerminalColorStyle.Colors.BlueBright,
                            TerminalColorStyle.Colors.Blue
                        )
                    )
                    else -> Grass(
                        "\"",
                        TerminalColorStyle.TerminalStyle(
                            TerminalColorStyle.Colors.GreenBright,
                            TerminalColorStyle.Colors.Green
                        )
                    )
                }
            }
        }.let { List2D.from(it) }
}