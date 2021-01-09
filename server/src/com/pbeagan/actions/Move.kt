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
import com.pbeagan.util.Coord
import com.pbeagan.util.coord
import com.pbeagan.util.exhaustive

class Move private constructor(private val directionList: List<Direction>) : Action() {
    override fun invoke(self: Mob) {
        directionList.forEach { direction ->
            when (val checkMove = checkSpace(
                direction,
                self.locationInRoom.x,
                self.locationInRoom.y,
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
    ): Coord {
        val boundX = (0..currentRoom.terrain.first().size).average().toInt()
        val boundY = (0..currentRoom.terrain.size).average().toInt()
        return when (direction) {
            NORTH -> boundX coord 0
            SOUTH -> boundX coord currentRoom.height - 1
            EAST -> 0 coord boundY
            WEST -> currentRoom.width - 1 coord boundY
            UP -> boundX coord boundY
            DOWN -> boundX coord boundY
        }
    }

    fun checkEdgeSpacesToLandInRoom(
        direction: Direction,
        xy: Coord,
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
                (trial.x + if (isGoingUp) counter else -counter) coord trial.y
            } else {
                trial.x coord (trial.y + if (isGoingUp) counter else -counter)
            }
            isGoingUp = !isGoingUp
            counter++
        }
    }

    sealed class Result {
        class Point(val data: Coord) : Result()
        class RoomId(val data: Int) : Result()
        object EndOfRoomFailure : Result()
        class Failure(val reason: String) : Result()
    }

    companion object {
        private val INVALID_MOVE = -1 coord -1

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
            newSpace: Coord,
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
            newSpace: Coord
        ): Boolean {
            val yBound = layout.size - 1
            val xBound = layout.first().size - 1
            return newSpace.x in 0..xBound && newSpace.y in 0..yBound
        }

        private fun getNewSpace(
            direction: Direction,
            x: Int,
            y: Int
        ): Coord = when (direction) {
            NORTH -> x coord y + 1
            EAST -> x + 1 coord y
            SOUTH -> x coord y - 1
            WEST -> x - 1 coord y
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
                    location.x,
                    location.y,
                    self.currentRoom()!!.terrain,
                    self.currentRoom()!!
                )) {
                    is EndOfRoomFailure -> getNextRoom(direction, self.currentRoom())
                        ?: return Retry("There is no room to enter in that direction.")
                    is Failure -> return Retry("Sorry, can't go that way. ${checkMove.reason}")
                    else -> {
                        location = getNewSpace(direction, location.x, location.y)
                    }
                }
            }
            return Move(directionList)
        }

        fun forceMove(vararg direction: Direction) = Move(direction.toList())
    }
}