package com.pbeagan.actions

import com.pbeagan.data.Direction
import com.pbeagan.data.Direction.DOWN
import com.pbeagan.data.Direction.EAST
import com.pbeagan.data.Direction.NORTH
import com.pbeagan.data.Direction.SOUTH
import com.pbeagan.data.Direction.UP
import com.pbeagan.data.Direction.WEST
import com.pbeagan.data.ItemFlags.COLLISION
import com.pbeagan.data.Mob
import com.pbeagan.data.MobBehavior
import com.pbeagan.data.RoomDirectionData
import com.pbeagan.data.currentRoom
import com.pbeagan.util.exhaustive

class Move private constructor(private val direction: Direction) : Action() {
    override fun invoke(self: Mob) {
        when (val checkMove = checkMove(self, direction)) {
            is Result.Point -> self.locationInRoom = checkMove.data
            is Result.EndOfRoomFailure -> getNextRoom(self, direction)?.let { changeRooms(self, it, direction) }
                ?: Retry("There is no room to enter in that direction.")
            is Result.Failure -> Retry("Sorry, can't go that way. ${checkMove.reason}")
            is Result.RoomId -> self.location = checkMove.data
        }.exhaustive
    }

    private fun changeRooms(
        self: Mob,
        room: RoomDirectionData,
        direction: Direction
    ) {
        writer.sayToRoomOf(self).move("${self.name} left ${this.direction.name}")
        self.location = room.destinationID
        val (x, y) = self.locationInRoom
        self.locationInRoom = when (direction) {
            NORTH -> x to 0
            SOUTH -> x to (self.currentRoom()?.height?.minus(1) ?: 0)
            EAST -> 0 to y
            WEST -> (self.currentRoom()?.width?.minus(1) ?: 0) to y
            UP -> x to y
            DOWN -> x to y
        }
        writer.sayToRoomOf(self).move("${self.name} arrived from ${this.direction.inverse().name}")

        recordVisit(self)

        if (self.isPlayer) {
            Look().also { it.writer = writer }(self)
        }
    }

    sealed class Result {
        class Point(val data: Pair<Int, Int>) : Result()
        class RoomId(val data: Int) : Result()
        object EndOfRoomFailure : Result()
        class Failure(val reason: String) : Result()
    }

    companion object {
        private val INVALID_MOVE = -1 to -1

        fun checkMove(
            self: Mob,
            direction: Direction
        ): Result {

            val currentRoom = self.currentRoom()
            val layout = currentRoom?.terrain ?: return Result.EndOfRoomFailure

            val (x, y) = self.locationInRoom
            val newSpace = getNewSpace(direction, x, y)

            val yBound = layout.size - 1
            val xBound = layout.first().size - 1
            val isInRoom = newSpace.first in 0..xBound && newSpace.second in 0..yBound
            if (!isInRoom) return Result.EndOfRoomFailure

            val (terrain, mobs, items) = currentRoom.getLocation(newSpace)

            val notTraversableMessage = terrain.notTraversableMessage
            if (notTraversableMessage != null) return Result.Failure(notTraversableMessage)

//                mobs.isNotEmpty() -> "There are mobs in that location."
            if (items.any { it.itemFlags.contains(COLLISION) }) return Result.Failure("There is an item in the way.")
            if (self.behavior == MobBehavior.IMMOBILE) return Result.Failure("You appear to be immobile.")

            return Result.Point(newSpace)
        }

        private fun getNewSpace(
            direction: Direction,
            x: Int,
            y: Int
        ): Pair<Int, Int> = when (direction) {
            NORTH -> x to y + 1
            EAST -> x + 1 to y
            SOUTH -> x to y - 1
            WEST -> x - 1 to y
            UP -> INVALID_MOVE
            DOWN -> INVALID_MOVE
        }

        private fun getNextRoom(self: Mob, direction: Direction) = self.currentRoom()
            ?.exits
            ?.firstOrNull { it.direction == direction }

        private fun recordVisit(self: Mob) {
            val visited = self.visited
            if (!visited.contains(self.location)) {
                visited.add(self.location)
            }
        }

        fun getOrRetry(self: Mob, direction: Direction): Action =
            when (val checkMove = checkMove(self, direction)) {
                is Result.EndOfRoomFailure -> getNextRoom(self, direction)?.let { Move(direction) }
                    ?: Retry("There is no room to enter in that direction.")
                is Result.Failure -> Retry("Sorry, can't go that way. ${checkMove.reason}")
                else -> Move(direction)
            }

        fun forceMove(direction: Direction) = Move(direction)
    }
}