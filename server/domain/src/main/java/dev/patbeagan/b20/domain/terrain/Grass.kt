package dev.patbeagan.b20.domain.terrain

import com.pbeagan.consolevision.TerminalColorStyle
import dev.patbeagan.b20.domain.terrain.type.Terrain

class Grass(override val prettySymbols: Array<String>, override val style: TerminalColorStyle.TerminalStyle) : Terrain() {
    override val symbol: Char = SYMBOL
    override val notTraversableMessage: String? = null

    constructor(prettySymbol: String, style: TerminalColorStyle.TerminalStyle = TerminalColorStyle.TerminalStyle()) : this(arrayOf(prettySymbol), style)

    companion object {
        const val SYMBOL = '\''
    }
}