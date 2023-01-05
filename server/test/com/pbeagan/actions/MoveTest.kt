package com.pbeagan.actions

import com.pbeagan.domain.types.Direction
import com.pbeagan.domain.terrain.Grass
import com.pbeagan.domain.terrain.type.Terrain

import org.junit.Assert
import org.junit.jupiter.api.Test

internal class MoveTest {

    val move = Move.forceMove(Direction.NORTH)
    val terrain = Array(8) { Array<Terrain>(8) { Grass("\"") } }

    @Test
    fun `check surroundings alternates between north and south`() {
        val list = move
            .checkEdgeSpacesToLandInRoom(Direction.EAST, 0 coord 4, terrain)
            .take(5).toList().toTypedArray()

        Assert.assertArrayEquals(
            arrayOf(
                0 to 4,
                0 to 5,
                0 to 3,
                0 to 6,
                0 to 2
            ), list
        )
    }

    @Test
    fun `check surroundings includes whole side`() {
        val list = move
            .checkEdgeSpacesToLandInRoom(Direction.EAST, 0 coord 0, terrain)
            .take(3).toList().toTypedArray()

        Assert.assertArrayEquals(
            arrayOf(
                0 to 0,
                0 to 1,
                0 to 2
            ), list
        )
    }

    @Test
    fun `check surroundings alternates between east and west`() {
        val list = move
            .checkEdgeSpacesToLandInRoom(Direction.SOUTH, 4 coord 7, terrain)
            .take(5).toList().toTypedArray()

        Assert.assertArrayEquals(
            arrayOf(
                4 to 7,
                5 to 7,
                3 to 7,
                6 to 7,
                2 to 7
            ), list
        )
    }

    @Test
    fun `check surroundings does not go past the bounds of the room`() {
        val checkSurroundingsHori = move
            .checkEdgeSpacesToLandInRoom(Direction.SOUTH, 4 coord 7, terrain)

        Assert.assertTrue(checkSurroundingsHori.all {
            it.x in 0..7 && it.y in 0..7
        })

        val checkSurroundingsVert = move
            .checkEdgeSpacesToLandInRoom(Direction.EAST, 0 coord 4, terrain)

        Assert.assertTrue(checkSurroundingsVert.all {
            it.x in 0..7 && it.y in 0..7
        })
    }
}