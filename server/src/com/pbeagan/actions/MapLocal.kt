package com.pbeagan.actions

import com.pbeagan.data.Direction
import com.pbeagan.data.Direction.EAST
import com.pbeagan.data.Direction.NORTH
import com.pbeagan.data.Direction.SOUTH
import com.pbeagan.data.Direction.WEST
import com.pbeagan.data.Exits
import com.pbeagan.data.ItemData
import com.pbeagan.data.Mob
import com.pbeagan.data.RoomData
import com.pbeagan.data.Terrain
import com.pbeagan.data.currentRoom
import com.pbeagan.util.Coord
import com.pbeagan.util.coord
import com.pbeagan.util.traverseMapIndexed
import com.pbeagan.writer.TerminalColorStyle.Colors.Red
import com.pbeagan.writer.TerminalColorStyle.Colors.YellowBright
import com.pbeagan.writer.TerminalColorStyle.style

typealias RoomMap = Array<Array<Triple<Terrain, List<Mob>, List<ItemData>>>>

class MapLocal : Action(), FreeAction {
    override fun invoke(self: Mob) {
        val currentRoom = self.currentRoom()
        val terrain = currentRoom?.terrain ?: return
        val vision = self.vision(currentRoom.lighting)
        printMap(vision, fillRoomMap(terrain, currentRoom), self, currentRoom.exits)
    }

    private fun fillRoomMap(
        roomMap: Array<Array<Terrain>>,
        currentRoom: RoomData
    ): RoomMap = roomMap.traverseMapIndexed { x, y, _ ->
        currentRoom.getLocation(x coord y)
    }

    private fun printMap(
        vision: HashSet<Coord>,
        roomMap: RoomMap,
        self: Mob,
        exits: Exits
    ) {
        val checkExit = provideCheckExit(exits)
        val horizontalLine = StringBuilder().also { stringBuilder ->
            repeat((roomMap.map { it.size }.max() ?: 0)) { stringBuilder.append("═") }
        }.toString()

        checkExit(NORTH, {}) { writer.sayTo(self).localMap("╔$horizontalLine╗") }
        for (y in roomMap.size - 1 downTo 0) {
            val line = StringBuilder()
            checkExit(WEST, { line.append(' ') }) { line.append("║") }
            for (x in roomMap[y].indices) {
                getCoordContent(vision, line, x coord y, roomMap)
            }
            checkExit(EAST, { line.append(' ') }) { line.append("║") }
            writer.sayTo(self).localMap(line.toString())
        }
        checkExit(SOUTH, {}) { writer.sayTo(self).localMap("╚$horizontalLine╝") }
    }

    private fun provideCheckExit(exits: Exits) =
        { direction: Direction, successAction: () -> Unit, failAction: () -> Unit ->
            if (exits.get(direction) != null) successAction() else failAction()
        }

    private fun getCoordContent(
        vision: HashSet<Coord>,
        line: StringBuilder,
        xy: Coord,
        roomMap: RoomMap
    ) {
        val (x, y) = xy
        val (terrain, mobs, items) = roomMap[y][x]

        line.append(
            if (xy in vision) {
                when {
                    mobs.isNotEmpty() -> {
                        mobs.firstOrNull()?.nameBase?.chars()
                            ?.findFirst()
                            ?.asInt
                            ?.toFormattedChar()
                            ?.toString()
                            ?.style(YellowBright, terrain.style.colorBackground)
                    }
                    items.isNotEmpty() -> "*".style(Red, terrain.style.colorBackground)
                    else -> terrain.prettySymbols.random().style(terrain.style)
                }
            } else {
                " "
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
