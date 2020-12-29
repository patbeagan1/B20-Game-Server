package com.pbeagan.data

import com.pbeagan.util.FlagCombined
import com.pbeagan.util.createFlagSet
import mobs

class RoomData constructor(
    val id: Int,
    val name: String,
    override val descriptionLook: String,
    override val descriptionPreview: (Direction) -> String,
    var exits: Exits,
    var roomFlags: FlagCombined<RoomFlags> = createFlagSet(),
    var weather: Weather = Weather.CLEAR,
    var lighting: Lighting = Lighting.BRIGHT,
    var items: MutableCollection<ItemData> = mutableListOf(),
    terrainString: String
) : Lookable {
    val terrain: Array<Array<Terrain>> = TerrainParser.parse(terrainString)

    fun getLocation(xy: Pair<Int, Int>) = Triple(
        terrain[xy.second][xy.first],
        mobs.filter { it.location == id && it.locationInRoom == xy },
        items.filter { it.locationInRoom == xy }
    )

    val height get() = terrain.size
    val width get() = terrain.firstOrNull()?.size ?: 0
}