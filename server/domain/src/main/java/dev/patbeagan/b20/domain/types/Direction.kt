package dev.patbeagan.b20.domain.types

enum class Direction {
    NORTH,
    EAST,
    SOUTH,
    WEST,
    UP,
    DOWN;

    fun inverse() = when (this) {
        NORTH -> SOUTH
        EAST -> WEST
        SOUTH -> NORTH
        WEST -> EAST
        UP -> DOWN
        DOWN -> UP
    }
}