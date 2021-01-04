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
import com.pbeagan.data.Mob
import com.pbeagan.data.MobBehavior
import com.pbeagan.data.RoomData
import com.pbeagan.data.RoomDirectionData
import com.pbeagan.data.Terrain
import com.pbeagan.data.currentRoom
import com.pbeagan.util.exhaustive

class Move private constructor(private val directionList: List<Direction>) : Action() {
    override fun invoke(self: Mob) {
        directionList.forEach { direction ->
            when (val checkMove = checkSpace(
                direction,
                self.locationInRoom.first,
                self.locationInRoom.second,
                self.currentRoom()!!.terrain,
                self.currentRoom()!!
            )) {
                is Point -> self.locationInRoom = checkMove.data
                is EndOfRoomFailure -> getNextRoom(direction, self.currentRoom())?.let {
                    changeRooms(
                        self,
                        it,
                        direction
                    )
                }
                    ?: Retry("There is no room to enter in that direction.")
                is Failure -> Retry("Sorry, can't go that way. ${checkMove.reason}")
                is RoomId -> self.location = checkMove.data
            }.exhaustive
        }
    }

    private fun changeRooms(
        self: Mob,
        room: RoomDirectionData,
        direction: Direction
    ) {
        writer.sayToRoomOf(self).move("${self.nameStyled} left ${direction.name}")
        self.location = room.destinationID
        val (x, y) = self.locationInRoom
        val currentRoom = self.currentRoom() ?: return
        self.locationInRoom = moveToEntranceOfRoom(direction, x, currentRoom, y)

        checkEdgeSpacesToLandInRoom(
            direction,
            self.locationInRoom,
            currentRoom.terrain
        ).first {
            writer.debug(it.toString())
            checkNewSpace(
                currentRoom.terrain,
                it,
                currentRoom
            ) !is Failure
        }.let {
            self.locationInRoom = it
        }

        writer.sayToRoomOf(self).move("${self.nameStyled} arrived from ${direction.inverse().name}")

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
        val boundX = (0..currentRoom.terrain.first().size).average().toInt()
        val boundY = (0..currentRoom.terrain.size).average().toInt()
        return when (direction) {
            NORTH -> boundX to 0
            SOUTH -> boundX to currentRoom.height - 1
            EAST -> 0 to boundY
            WEST -> currentRoom.width - 1 to boundY
            UP -> boundX to boundY
            DOWN -> boundX to boundY
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

        val failuresAllowed = if (!shouldScanHorizontal) terrain.size else terrain.first().size

        // we can allow one direction to go out of bounds, because the other is still valid
        // but if both of them are out of bounds, exit.
        var failures = 0
        while (failures < failuresAllowed) {
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
            direction: Direction,
            roomData: RoomData?
        ): Result {

            if (self.behavior == MobBehavior.IMMOBILE) return Failure("You appear to be immobile.")
            val layout = roomData?.terrain ?: return EndOfRoomFailure

            val (x, y) = self.locationInRoom
            return checkSpace(direction, x, y, layout, roomData)
        }

        private fun checkSpace(
            direction: Direction,
            x: Int,
            y: Int,
            layout: Array<Array<Terrain>>,
            currentRoom: RoomData
        ): Result {
            val newSpace = getNewSpace(direction, x, y)

            return checkNewSpace(layout, newSpace, currentRoom)
        }

        private fun checkNewSpace(
            layout: Array<Array<Terrain>>,
            newSpace: Pair<Int, Int>,
            currentRoom: RoomData
        ): Result {
            val isInRoom = checkRoomBounds(layout, newSpace)
            if (!isInRoom) return EndOfRoomFailure

            val (terrain, mobs, items) = currentRoom.getLocation(newSpace)

            val notTraversableMessage = terrain.notTraversableMessage
            if (notTraversableMessage != null) return Failure(notTraversableMessage)

            // Blocking by mobs is situational, because their position is unpredictable.
            // The same goes for items, if they are dropped.

            // if (mobs.isNotEmpty()) return Failure("There are mobs in that location.")
            // if (items.any { it.itemFlags.contains(COLLISION) }) return Failure("There is an item in the way.")

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

        private fun getNextRoom(direction: Direction, roomData: RoomData?) =
            roomData?.exits?.get(direction)

        private fun recordVisit(self: Mob) {
            val visited = self.visited
            if (!visited.contains(self.location)) {
                visited.add(self.location)
            }
        }

        fun getOrRetry(self: Mob, directionList: List<Direction>): Action {
            var location = self.locationInRoom
            directionList.forEach { direction ->
                when (val checkMove = checkSpace(
                    direction,
                    location.first,
                    location.second,
                    self.currentRoom()!!.terrain,
                    self.currentRoom()!!
                )) {
                    is EndOfRoomFailure -> getNextRoom(direction, self.currentRoom())
                        ?: return Retry("There is no room to enter in that direction.")
                    is Failure -> return Retry("Sorry, can't go that way. ${checkMove.reason}")
                    else -> {
                        location = getNewSpace(direction, location.first, location.second)
                    }
                }
            }
            return Move(directionList)
        }

        fun forceMove(vararg direction: Direction) = Move(direction.toList())
    }
}