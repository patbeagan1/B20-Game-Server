package com.pbeagan.domain.terrain

import com.pbeagan.consolevision.TerminalColorStyle
import com.pbeagan.domain.terrain.type.Terrain

class Water(override val prettySymbols: Array<String>, override val style: TerminalColorStyle.TerminalStyle) : Terrain() {
    override val symbol: Char = SYMBOL
    override val notTraversableMessage: String? = "There is water here!"

    constructor(prettySymbol: String, style: TerminalColorStyle.TerminalStyle = TerminalColorStyle.TerminalStyle()) : this(arrayOf(prettySymbol), style)

    companion object {
        const val SYMBOL = '~'
    }
}