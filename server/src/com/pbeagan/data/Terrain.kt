package com.pbeagan.data

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
                    Grass.SYMBOL -> Grass()
                    Pavement.SYMBOL -> Pavement()
                    Wall.SYMBOL -> Wall()
                    Water.SYMBOL -> Water()
                    else -> Grass()
                }
            }.toTypedArray()
        }.toTypedArray()
}

// get greg's dnd books


abstract class Terrain {
    abstract val symbol: Char
    abstract val notTraversableMessage: String?
}

class Grass : Terrain() {
    override val symbol: Char = SYMBOL
    override val notTraversableMessage: String? = null

    companion object {
        const val SYMBOL = '\''
    }
}

class Pavement : Terrain() {
    override val symbol: Char = SYMBOL
    override val notTraversableMessage: String? = null

    companion object {
        const val SYMBOL = '-'
    }
}

class Wall : Terrain() {
    override val symbol: Char = SYMBOL
    override val notTraversableMessage: String? = "There is a wall there!"

    companion object {
        const val SYMBOL = '*'
    }
}

class Water : Terrain() {
    override val symbol: Char = SYMBOL
    override val notTraversableMessage: String? = "There is water here!"

    companion object {
        const val SYMBOL = '~'
    }
}
