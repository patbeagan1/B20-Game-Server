package com.pbeagan.domain.terrain.type

import com.pbeagan.consolevision.TerminalColorStyle.TerminalStyle

abstract class Terrain {
    abstract val symbol: Char
    abstract val prettySymbols: Array<String>
    abstract val notTraversableMessage: String?
    abstract val style: TerminalStyle
}
