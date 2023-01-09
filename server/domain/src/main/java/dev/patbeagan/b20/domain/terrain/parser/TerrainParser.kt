package dev.patbeagan.b20.domain.terrain.parser

import dev.patbeagan.b20.domain.terrain.Grass
import dev.patbeagan.b20.domain.terrain.Pavement
import dev.patbeagan.b20.domain.terrain.Wall
import dev.patbeagan.b20.domain.terrain.Water
import dev.patbeagan.consolevision.TerminalStyle
import dev.patbeagan.consolevision.ansi.AnsiColor
import dev.patbeagan.consolevision.ansi.AnsiSGR
import dev.patbeagan.consolevision.types.ColorInt
import dev.patbeagan.consolevision.types.CompressionStyle
import dev.patbeagan.consolevision.types.List2D
import kotlin.random.Random

object TerrainParser {

    private fun ColorInt.mutate(variance: Int): ColorInt {
        fun Int.newColorVal() = this + (Random.nextInt() % variance).coerceIn(0..255)
        return ColorInt.from(
            0,
            this.colorRed.newColorVal(),
            this.colorGreen.newColorVal(),
            this.colorBlue.newColorVal()
        )
    }

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
            rows.toCharArray().map { char ->
                when (char) {
                    Grass.SYMBOL -> Grass(
                        arrayOf("\"", "\"", "\"", "\"", "'", "*"),
                        TerminalStyle(
                            ColorInt.from(256, 34, 139, 34)
                                .mutate(50)
                                .let { AnsiColor.Custom(it) },
                            ColorInt.from(256, 34, 110, 34)
                                .let { AnsiColor.Custom(it) },
                            AnsiSGR.Bold
                        )
                    )

                    Pavement.SYMBOL -> Pavement(
                        ".",
                        TerminalStyle(
                            AnsiColor.Black1,
                            AnsiColor.Black2
                        )
                    )

                    Wall.SYMBOL -> Wall(
                        CompressionStyle.DOTS_HIGH.symbol,
                        TerminalStyle(
                            AnsiColor.Black,
                            AnsiColor.Black1
                        )
                    )

                    Water.SYMBOL -> Water(
                        "⍨",
                        TerminalStyle(
                            AnsiColor.BlueBright,
                            AnsiColor.Blue
                        )
                    )

                    else -> Grass(
                        "\"",
                        TerminalStyle(
                            AnsiColor.GreenBright,
                            AnsiColor.Green
                        )
                    )
                }
            }
        }.let { List2D.from(it) }
}