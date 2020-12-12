package com.pbeagan.actions

import com.pbeagan.data.Direction
import com.pbeagan.data.Mob
import com.pbeagan.data.RoomDirectionData
import com.pbeagan.data.Terrain
import com.pbeagan.data.currentRoom
import com.pbeagan.data.currentRoomOtherMobsAndSelf
import com.pbeagan.util.cloneStructure
import mobs

class LocalMap : Action(), FreeAction {
    override fun invoke(self: Mob) {
        val mobsAndSelf = self.currentRoomOtherMobsAndSelf(mobs)
        val currentRoom = self.currentRoom()
        val terrain = currentRoom?.terrain ?: return
        val mobsInRoom = terrain.cloneStructure<Terrain, MutableList<Mob>?> { null }

        mobsAndSelf.forEach { mob ->
            val (x, y) = mob.locationInRoom
            writer.debug("${mob.name} ${mob.locationInRoom}")
            mobsInRoom[y][x]?.add(mob) ?: run { mobsInRoom[y][x] = arrayListOf(mob) }
        }

        printMap(terrain, self, mobsInRoom, currentRoom.exits)
    }

    private fun printMap(
        terrain: Array<Array<Terrain>>,
        self: Mob,
        mobsInRoom: Array<Array<MutableList<Mob>?>>,
        exits: List<RoomDirectionData>
    ) {
        val checkExit = provideCheckExit(exits)
        val horizontalLine = StringBuilder().also { stringBuilder ->
            repeat((terrain.map { it.size }.max() ?: 0) + 2) { stringBuilder.append("=") }
        }.toString()

        checkExit(Direction.NORTH, {}) {
            writer.sayTo(self).localMap(horizontalLine)
        }
        for (y in terrain.size - 1 downTo 0) {
            val line = StringBuilder()
            checkExit(Direction.WEST, { line.append(' ') }) { line.append("|") }
            for (x in terrain[y].indices) {
                getCoordContent(line, x to y, mobsInRoom, terrain)
            }
            checkExit(Direction.EAST, { line.append(' ') }) { line.append("|") }
            writer.sayTo(self).localMap(line.toString())
        }
        checkExit(Direction.SOUTH, {}) {
            writer.sayTo(self).localMap(horizontalLine)
        }
    }

    private fun provideCheckExit(exits: List<RoomDirectionData>) =
        { direction: Direction, successAction: () -> Unit, failAction: () -> Unit ->
            if (exits.any { it.direction == direction }) successAction() else failAction()
        }

    private fun getCoordContent(
        line: StringBuilder,
        xy: Pair<Int, Int>,
        mobsInRoom: Array<Array<MutableList<Mob>?>>,
        terrain: Array<Array<Terrain>>
    ) {
        val (x, y) = xy
        line.append(
            if (mobsInRoom[y][x]?.isNotEmpty() == true) {
                mobsInRoom[y][x]?.firstOrNull()?.name?.chars()?.findFirst()?.asInt?.toFormattedChar()
            } else {
                terrain[y][x].symbol
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