package com.pbeagan.contextual.actions

import com.pbeagan.WorldState
import com.pbeagan.consolevision.Coord
import com.pbeagan.consolevision.List2D
import com.pbeagan.consolevision.TerminalColorStyle.Colors.Red
import com.pbeagan.consolevision.TerminalColorStyle.Colors.YellowBright
import com.pbeagan.consolevision.TerminalColorStyle.style
import com.pbeagan.consolevision.coord
import com.pbeagan.contextual.Mob
import com.pbeagan.contextual.RoomData
import com.pbeagan.contextual.actions.type.Action
import com.pbeagan.contextual.actions.type.FreeAction
import dev.patbeagan.b20.domain.Exits
import dev.patbeagan.b20.domain.RoomTile
import dev.patbeagan.b20.domain.terrain.type.Terrain
import dev.patbeagan.b20.domain.types.Direction
import dev.patbeagan.b20.domain.types.Direction.EAST
import dev.patbeagan.b20.domain.types.Direction.NORTH
import dev.patbeagan.b20.domain.types.Direction.SOUTH
import dev.patbeagan.b20.domain.types.Direction.WEST

class MapLocal(val worldState: WorldState) : Action(), FreeAction {
    override fun invoke(self: Mob) = with(worldState) {
        val currentRoom = self.currentRoom()
        val terrain = currentRoom?.terrain ?: return
        val vision = self.vision(currentRoom.lighting)
        printMap(vision, fillRoomMap(terrain, currentRoom), self, currentRoom.exits)
    }

    private fun fillRoomMap(
        roomMap: List2D<Terrain>,
        currentRoom: RoomData,
    ): List2D<RoomTile> = roomMap.traverseMapIndexed { x, y, _ ->
        currentRoom.run {
            worldState.getLocation(x coord y)
        }
    }

    private fun printMap(
        vision: HashSet<Coord>,
        roomMap: List2D<RoomTile>,
        self: Mob,
        exits: Exits,
    ) {
        val checkExit = provideCheckExit(exits)
        val horizontalLine = StringBuilder().also { stringBuilder ->
            repeat(roomMap.width) { stringBuilder.append("═") }
        }.toString()

        checkExit(NORTH, {}) { writer.sayTo(self).localMap("╔$horizontalLine╗") }
        for (y in roomMap.height - 1 downTo 0) {
            val line = StringBuilder()
            checkExit(WEST, { line.append(' ') }) { line.append("║") }
            for (x in 0 until roomMap.width) {
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
        roomMap: List2D<RoomTile>,
    ) {
        val (x, y) = xy
        val (terrain, mobs, items) = roomMap.at(x, y)

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
    in 'a'.code..'z'.code -> toChar()
    in 'A'.code..'Z'.code -> toChar()
    else -> ' '
}
