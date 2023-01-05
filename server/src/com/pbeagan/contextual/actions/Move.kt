package com.pbeagan.contextual.actions

import com.pbeagan.WorldState
import com.pbeagan.consolevision.Coord
import com.pbeagan.consolevision.List2D
import com.pbeagan.consolevision.coord
import com.pbeagan.contextual.Mob
import com.pbeagan.contextual.Mob.MobBehavior
import com.pbeagan.contextual.RoomData
import com.pbeagan.contextual.actions.Move.Result.EndOfRoomFailure
import com.pbeagan.contextual.actions.Move.Result.Failure
import com.pbeagan.contextual.actions.Move.Result.Point
import com.pbeagan.contextual.actions.Move.Result.RoomId
import com.pbeagan.contextual.actions.type.Action
import com.pbeagan.util.exhaustive
import dev.patbeagan.b20.domain.RoomDirectionData
import dev.patbeagan.b20.domain.terrain.type.Terrain
import dev.patbeagan.b20.domain.types.Direction
import dev.patbeagan.b20.domain.types.Direction.DOWN
import dev.patbeagan.b20.domain.types.Direction.EAST
import dev.patbeagan.b20.domain.types.Direction.NORTH
import dev.patbeagan.b20.domain.types.Direction.SOUTH
import dev.patbeagan.b20.domain.types.Direction.UP
import dev.patbeagan.b20.domain.types.Direction.WEST

class Move private constructor(
    private val directionList: List<Direction>,
    val worldState: WorldState,
) : Action() {
    override fun invoke(self: Mob) = with(worldState) {
        directionList.forEach { direction ->
            when (val checkMove = checkSpace(
                direction,
                self.locationInRoom.x,
                self.locationInRoom.y,
                self.currentRoom()!!.terrain,
                self.currentRoom()!!,
                worldState
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
                is RoomId -> self.room = checkMove.data
            }.exhaustive
        }
    }

    private fun changeRooms(
        self: Mob,
        room: RoomDirectionData,
        direction: Direction,
    ) = with(worldState) {
        writer.sayToRoomOf(self).move("${self.nameStyled} left ${direction.name}")
        self.room = room.destinationID
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
                currentRoom,
                worldState
            ) !is Failure
        }.let {
            self.locationInRoom = it
        }

        writer.sayToRoomOf(self).move("${self.nameStyled} arrived from ${direction.inverse().name}")

        recordVisit(self)

        if (self.isPlayer) {
            Look(worldState).also { it.writer = writer }(self)
        }
    }

    private fun moveToEntranceOfRoom(
        direction: Direction,
        x: Int,
        currentRoom: RoomData,
        y: Int,
    ): Coord {
        val boundX = (0..currentRoom.terrain.width).average().toInt()
        val boundY = (0..currentRoom.terrain.height).average().toInt()
        return when (direction) {
            NORTH -> boundX coord 0
            SOUTH -> boundX coord currentRoom.terrain.height - 1
            EAST -> 0 coord boundY
            WEST -> currentRoom.terrain.width - 1 coord boundY
            UP -> boundX coord boundY
            DOWN -> boundX coord boundY
        }
    }

    fun checkEdgeSpacesToLandInRoom(
        direction: Direction,
        xy: Coord,
        terrain: List2D<Terrain>,
    ) = sequence {
        var isGoingUp = true
        val shouldScanHorizontal = when (direction) {
            NORTH, SOUTH -> true
            EAST, WEST -> false
            UP, DOWN -> TODO()
        }
        var trial = xy.copy()
        var counter = 1

        val failuresAllowed = if (!shouldScanHorizontal) terrain.height else terrain.width

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
            roomData: RoomData?,
            worldState: WorldState,
        ): Result {

            if (self.behavior == MobBehavior.IMMOBILE) return Failure("You appear to be immobile.")
            val layout = roomData?.terrain ?: return EndOfRoomFailure

            val (x, y) = self.locationInRoom
            return checkSpace(direction, x, y, layout, roomData, worldState)
        }

        private fun checkSpace(
            direction: Direction,
            x: Int,
            y: Int,
            layout: List2D<Terrain>,
            currentRoom: RoomData,
            worldState: WorldState,
        ): Result {
            val newSpace = getNewSpace(direction, x, y)

            return checkNewSpace(layout, newSpace, currentRoom, worldState)
        }

        private fun checkNewSpace(
            layout: List2D<Terrain>,
            newSpace: Coord,
            currentRoom: RoomData,
            worldState: WorldState,
        ): Result {
            val isInRoom = checkRoomBounds(layout, newSpace)
            if (!isInRoom) return EndOfRoomFailure

            val (terrain, mobs, items) = currentRoom.run {
                worldState.getLocation(newSpace)
            }

            val notTraversableMessage = terrain.notTraversableMessage
            if (notTraversableMessage != null) return Failure(notTraversableMessage)

            // Blocking by mobs is situational, because their position is unpredictable.
            // The same goes for items, if they are dropped.

            // if (mobs.isNotEmpty()) return Failure("There are mobs in that location.")
            // if (items.any { it.itemFlags.contains(COLLISION) }) return Failure("There is an item in the way.")

            return Point(newSpace)
        }

        private fun checkRoomBounds(
            layout: List2D<Terrain>,
            newSpace: Coord,
        ): Boolean {

            return newSpace.x in 0 until layout.width && newSpace.y in 0 until layout.height
        }

        private fun getNewSpace(
            direction: Direction,
            x: Int,
            y: Int,
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
            if (!visited.contains(self.room)) {
                visited.add(self.room)
            }
        }

        fun getOrRetry(self: Mob, directionList: List<Direction>, worldState: WorldState): Action {
            var location = self.locationInRoom
            directionList.forEach { direction ->
                with(worldState) {
                    when (val checkMove = checkSpace(
                        direction,
                        location.x,
                        location.y,
                        self.currentRoom()!!.terrain,
                        self.currentRoom()!!,
                        worldState
                    )) {
                        is EndOfRoomFailure -> getNextRoom(direction, self.currentRoom())
                            ?: return Retry("There is no room to enter in that direction.")

                        is Failure -> return Retry("Sorry, can't go that way. ${checkMove.reason}")
                        else -> {
                            location = getNewSpace(direction, location.x, location.y)
                        }
                    }
                }
            }
            return Move(directionList, worldState)
        }

        fun forceMove(worldState: WorldState, vararg direction: Direction) =
            Move(direction.toList(), worldState)
    }
}