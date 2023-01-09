package dev.patbeagan.b20.contextual.actions

import dev.patbeagan.b20.contextual.Mob
import dev.patbeagan.b20.contextual.RoomData
import com.pbeagan.contextual.actions.type.Action
import com.pbeagan.contextual.actions.type.FreeAction
import dev.patbeagan.b20.WorldState
import dev.patbeagan.b20.domain.Exits
import dev.patbeagan.b20.domain.RoomTile
import dev.patbeagan.b20.domain.terrain.type.Terrain
import dev.patbeagan.b20.domain.types.Direction
import dev.patbeagan.b20.domain.types.Direction.EAST
import dev.patbeagan.b20.domain.types.Direction.NORTH
import dev.patbeagan.b20.domain.types.Direction.SOUTH
import dev.patbeagan.b20.domain.types.Direction.WEST
import dev.patbeagan.consolevision.ansi.AnsiColor
import dev.patbeagan.consolevision.style
import dev.patbeagan.consolevision.types.CompressedPoint
import dev.patbeagan.consolevision.types.List2D
import dev.patbeagan.consolevision.types.coord

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
        vision: HashSet<CompressedPoint>,
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
            if (exits.check(direction) != null) successAction() else failAction()
        }

    private fun getCoordContent(
        vision: HashSet<CompressedPoint>,
        line: StringBuilder,
        xy: CompressedPoint,
        roomMap: List2D<RoomTile>,
    ) {
        val (terrain, mobs, items) = roomMap.at(xy. x,xy.  y)

        line.append(
            // todo reenable vision
//            if (xy in vision) {
                when {
                    mobs.isNotEmpty() -> {
                        mobs.firstOrNull()?.nameBase?.chars()
                            ?.findFirst()
                            ?.asInt
                            ?.toFormattedChar()
                            ?.toString()
                            ?.style(AnsiColor.YellowBright, terrain.style.colorBackground)
                    }

                    items.isNotEmpty() -> "*".style(AnsiColor.Red, terrain.style.colorBackground)
                    else -> terrain.prettySymbols.random().style(terrain.style)
                }
//            } else {
//                " "
//            }
        )
    }
}

fun Int?.toFormattedChar() = when (this) {
    null -> ' '
    in 'a'.code..'z'.code -> toChar()
    in 'A'.code..'Z'.code -> toChar()
    else -> ' '
}
