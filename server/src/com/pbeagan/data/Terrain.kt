package com.pbeagan.data

import com.pbeagan.util.List2D
import com.pbeagan.consolevision.TerminalColorStyle.Colors
import com.pbeagan.consolevision.TerminalColorStyle.Colors.Green
import com.pbeagan.consolevision.TerminalColorStyle.Colors.GreenBright
import com.pbeagan.consolevision.TerminalColorStyle.DOTS_HIGH
import com.pbeagan.consolevision.TerminalColorStyle.SGR
import com.pbeagan.consolevision.TerminalColorStyle.TerminalStyle

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
                        TerminalStyle(
                            Colors.Custom(34, 139, 34).mutate(50),
                            Colors.Custom(34, 110, 34),
                            SGR.Bold
                        )
                    )
                    Pavement.SYMBOL -> Pavement(".", TerminalStyle(Colors.Black1, Colors.Black2))
                    Wall.SYMBOL -> Wall(DOTS_HIGH.toString(), TerminalStyle(Colors.Black, Colors.Black1))
                    Water.SYMBOL -> Water("⍨", TerminalStyle(Colors.BlueBright, Colors.Blue))
                    else -> Grass("\"", TerminalStyle(GreenBright, Green))
                }
            }
        }.let { List2D.from(it) }
}

abstract class Terrain {
    abstract val symbol: Char
    abstract val prettySymbols: Array<String>
    abstract val notTraversableMessage: String?
    abstract val style: TerminalStyle
}

class Grass(override val prettySymbols: Array<String>, override val style: TerminalStyle) : Terrain() {
    override val symbol: Char = SYMBOL
    override val notTraversableMessage: String? = null

    constructor(prettySymbol: String, style: TerminalStyle = TerminalStyle()) : this(arrayOf(prettySymbol), style)

    companion object {
        const val SYMBOL = '\''
    }
}

class Pavement(override val prettySymbols: Array<String>, override val style: TerminalStyle) : Terrain() {
    override val symbol: Char = SYMBOL
    override val notTraversableMessage: String? = null

    constructor(prettySymbol: String, style: TerminalStyle = TerminalStyle()) : this(arrayOf(prettySymbol), style)

    companion object {
        const val SYMBOL = '-'
    }
}

class Wall(override val prettySymbols: Array<String>, override val style: TerminalStyle) : Terrain() {
    override val symbol: Char = SYMBOL
    override val notTraversableMessage: String? = "There is a wall there!"

    constructor(prettySymbol: String, style: TerminalStyle = TerminalStyle()) : this(arrayOf(prettySymbol), style)

    companion object {
        const val SYMBOL = '*'
    }
}

class Water(override val prettySymbols: Array<String>, override val style: TerminalStyle) : Terrain() {
    override val symbol: Char = SYMBOL
    override val notTraversableMessage: String? = "There is water here!"

    constructor(prettySymbol: String, style: TerminalStyle = TerminalStyle()) : this(arrayOf(prettySymbol), style)

    companion object {
        const val SYMBOL = '~'
    }
}
