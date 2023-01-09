package dev.patbeagan.b20.domain.terrain

import dev.patbeagan.b20.domain.terrain.type.Terrain
import dev.patbeagan.consolevision.TerminalStyle

class Wall(
    override val prettySymbols: Array<String>,
    override val style: TerminalStyle,
) : Terrain() {
    override val symbol: Char = SYMBOL
    override val notTraversableMessage: String = "There is a wall there!"

    constructor(
        prettySymbol: String,
        style: TerminalStyle = TerminalStyle(),
    ) : this(arrayOf(prettySymbol), style)

    companion object {
        const val SYMBOL = '*'
    }
}