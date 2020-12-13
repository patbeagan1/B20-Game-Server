package com.pbeagan.actions

import com.pbeagan.actions.Move.Result.EndOfRoomFailure
import com.pbeagan.actions.Move.Result.Failure
import com.pbeagan.actions.Move.Result.Point
import com.pbeagan.actions.Move.Result.RoomId
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
import com.pbeagan.data.RoomData
import com.pbeagan.data.RoomDirectionData
import com.pbeagan.data.Terrain
import com.pbeagan.data.currentRoom
import com.pbeagan.util.exhaustive

class Move private constructor(private val direction: Direction) : Action() {
    override fun invoke(self: Mob) {
        when (val checkMove = checkMove(self, direction)) {
            is Point -> self.locationInRoom = checkMove.data
            is EndOfRoomFailure -> getNextRoom(self, direction)?.let { changeRooms(self, it, direction) }
                ?: Retry("There is no room to enter in that direction.")
            is Failure -> Retry("Sorry, can't go that way. ${checkMove.reason}")
            is RoomId -> self.location = checkMove.data
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
        val currentRoom = self.currentRoom() ?: return
        self.locationInRoom = moveToEntranceOfRoom(direction, x, currentRoom, y)

        checkEdgeSpacesToLandInRoom(
            direction,
            self.locationInRoom,
            currentRoom.terrain
        ).also {
            writer.debug(it.toString())
        }.first {
            checkNewSpace(
                currentRoom.terrain,
                it,
                currentRoom,
                self
            ) !is Failure
        }.let {
            self.locationInRoom = it
        }

        writer.sayToRoomOf(self).move("${self.name} arrived from ${this.direction.inverse().name}")

        recordVisit(self)

        if (self.isPlayer) {
            Look().also { it.writer = writer }(self)
        }
    }

    private fun moveToEntranceOfRoom(
        direction: Direction,
        x: Int,
        currentRoom: RoomData,
        y: Int
    ): Pair<Int, Int> {
        return when (direction) {
            NORTH -> x to 0
            SOUTH -> x to currentRoom.height - 1
            EAST -> 0 to y
            WEST -> currentRoom.width - 1 to y
            UP -> x to y
            DOWN -> x to y
        }
    }

    fun checkEdgeSpacesToLandInRoom(
        direction: Direction,
        xy: Pair<Int, Int>,
        terrain: Array<Array<Terrain>>
    ) = sequence {
        var isGoingUp = true
        val shouldScanHorizontal = when (direction) {
            NORTH, SOUTH -> true
            EAST, WEST -> false
            UP, DOWN -> TODO()
        }
        var trial = xy.copy()
        var counter = 1

        // we can allow one direction to go out of bounds, because the other is still valid
        // but if both of them are out of bounds, exit.
        var failures = 0
        while (failures < 2) {
            if (!checkRoomBounds(terrain, trial)) {
                failures++
            } else {
                writer.debug(trial.toString())
                yield(trial)
            }

            trial = if (shouldScanHorizontal) {
                (trial.first + if (isGoingUp) counter else -counter) to trial.second
            } else {
                trial.first to (trial.second + if (isGoingUp) counter else -counter)
            }
            isGoingUp = !isGoingUp
            counter++
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
            val layout = currentRoom?.terrain ?: return EndOfRoomFailure

            val (x, y) = self.locationInRoom
            val newSpace = getNewSpace(direction, x, y)

            return checkNewSpace(layout, newSpace, currentRoom, self)
        }

        private fun checkNewSpace(
            layout: Array<Array<Terrain>>,
            newSpace: Pair<Int, Int>,
            currentRoom: RoomData,
            self: Mob
        ): Result {
            val isInRoom = checkRoomBounds(layout, newSpace)
            if (!isInRoom) return EndOfRoomFailure

            val (terrain, mobs, items) = currentRoom.getLocation(newSpace)

            val notTraversableMessage = terrain.notTraversableMessage
            if (notTraversableMessage != null) return Failure(notTraversableMessage)
            if (mobs.isNotEmpty()) return Failure("There are mobs in that location.")
            if (items.any { it.itemFlags.contains(COLLISION) }) return Failure("There is an item in the way.")
            if (self.behavior == MobBehavior.IMMOBILE) return Failure("You appear to be immobile.")

            return Point(newSpace)
        }

        private fun checkRoomBounds(
            layout: Array<Array<Terrain>>,
            newSpace: Pair<Int, Int>
        ): Boolean {
            val yBound = layout.size - 1
            val xBound = layout.first().size - 1
            return newSpace.first in 0..xBound && newSpace.second in 0..yBound
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
                is EndOfRoomFailure -> getNextRoom(self, direction)?.let { Move(direction) }
                    ?: Retry("There is no room to enter in that direction.")
                is Failure -> Retry("Sorry, can't go that way. ${checkMove.reason}")
                else -> Move(direction)
            }

        fun forceMove(direction: Direction) = Move(direction)
    }
}