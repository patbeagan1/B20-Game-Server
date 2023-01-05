package dev.patbeagan.b20.domain

import dev.patbeagan.b20.domain.types.Direction

class Exits(
    private val north: RoomDirectionData? = null,
    private val south: RoomDirectionData? = null,
    private val east: RoomDirectionData? = null,
    private val west: RoomDirectionData? = null,
    private val up: RoomDirectionData? = null,
    private val down: RoomDirectionData? = null
) {
    fun check(name: Direction) = when (name) {
        Direction.NORTH -> north
        Direction.EAST -> east
        Direction.SOUTH -> south
        Direction.WEST -> west
        Direction.UP -> up
        Direction.DOWN -> down
    }

    val asList = Direction.values().mapNotNull { direction -> check(direction)?.let { direction to it } }
}