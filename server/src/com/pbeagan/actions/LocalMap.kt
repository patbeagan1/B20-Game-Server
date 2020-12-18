package com.pbeagan.actions

import com.pbeagan.data.Direction
import com.pbeagan.data.ItemData
import com.pbeagan.data.Mob
import com.pbeagan.data.RoomData
import com.pbeagan.data.RoomDirectionData
import com.pbeagan.data.Terrain
import com.pbeagan.data.currentRoom

typealias RoomMap = Array<Array<Triple<Terrain, List<Mob>, List<ItemData>>>>

class LocalMap : Action(), FreeAction {
    override fun invoke(self: Mob) {
        val currentRoom = self.currentRoom()
        val terrain = currentRoom?.terrain ?: return
        printMap(fillRoomMap(terrain, currentRoom), self, currentRoom.exits)
    }

    private fun fillRoomMap(
        roomMap: Array<Array<Terrain>>,
        currentRoom: RoomData
    ): RoomMap =
        roomMap.mapIndexed { y, arr ->
            arr.mapIndexed { x, _ ->
                currentRoom.getLocation(x to y)
            }.toTypedArray()
        }.toTypedArray()

    private fun printMap(
        roomMap: RoomMap,
        self: Mob,
        exits: List<RoomDirectionData>
    ) {
        val checkExit = provideCheckExit(exits)
        val horizontalLine = StringBuilder().also { stringBuilder ->
            repeat((roomMap.map { it.size }.max() ?: 0) + 2) { stringBuilder.append("=") }
        }.toString()

        checkExit(Direction.NORTH, {}) { writer.sayTo(self).localMap(horizontalLine) }
        for (y in roomMap.size - 1 downTo 0) {
            val line = StringBuilder()
            checkExit(Direction.WEST, { line.append(' ') }) { line.append("|") }
            for (x in roomMap[y].indices) {
                getCoordContent(line, x to y, roomMap)
            }
            checkExit(Direction.EAST, { line.append(' ') }) { line.append("|") }
            writer.sayTo(self).localMap(line.toString())
        }
        checkExit(Direction.SOUTH, {}) { writer.sayTo(self).localMap(horizontalLine) }
    }

    private fun provideCheckExit(exits: List<RoomDirectionData>) =
        { direction: Direction, successAction: () -> Unit, failAction: () -> Unit ->
            if (exits.any { it.direction == direction }) successAction() else failAction()
        }

    private fun getCoordContent(
        line: StringBuilder,
        xy: Pair<Int, Int>,
        roomMap: RoomMap
    ) {
        val (x, y) = xy
        val (terrain, mobs, items) = roomMap[y][x]
        line.append(
            when {
                mobs.isNotEmpty() -> {
                    mobs.firstOrNull()?.name?.chars()?.findFirst()?.asInt?.toFormattedChar()
                }
                items.isNotEmpty() -> "@"
                else -> terrain.symbol
            }
        )
    }
}

fun Int?.toFormattedChar() = when (this) {
    null -> ' '
    in 'a'.toInt()..'z'.toInt() -> toChar()
    in 'A'.toInt()..'Z'.toInt() -> toChar()
    else -> ' '
}
