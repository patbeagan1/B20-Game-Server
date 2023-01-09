package dev.patbeagan.b20.domain.terrain.type

import dev.patbeagan.consolevision.TerminalStyle

abstract class Terrain {
    abstract val symbol: Char
    abstract val prettySymbols: Array<String>
    abstract val notTraversableMessage: String?
    abstract val style: TerminalStyle
}
