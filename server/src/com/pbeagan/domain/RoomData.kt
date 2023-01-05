package com.pbeagan.domain

import com.pbeagan.domain.actions.RoomTile
import com.pbeagan.consolevision.Coord

import dev.patbeagan.base.FlagSet
import com.pbeagan.consolevision.List2D
import com.pbeagan.domain.flags.Lighting
import com.pbeagan.domain.flags.RoomFlags
import com.pbeagan.domain.terrain.type.Terrain
import com.pbeagan.domain.terrain.parser.TerrainParser
import com.pbeagan.domain.types.Direction
import com.pbeagan.domain.types.Weather
import mobs

class RoomData constructor(
    val id: Int,
    val name: String,
    override val descriptionLook: String,
    override val descriptionPreview: (Direction) -> String,
    var exits: Exits,
    var roomFlags: FlagSet<RoomFlags> = FlagSet.of(),
    var weather: Weather = Weather.CLEAR,
    var lighting: Lighting = Lighting.BRIGHT,
    var items: MutableCollection<ItemData> = mutableListOf(),
    terrainString: String,
) : Lookable {
    val terrain: List2D<Terrain> = TerrainParser.parse(terrainString)

    fun getLocation(xy: Coord) = RoomTile(
        terrain.at(xy.x, xy.y),
        mobs.filter { it.room == id && it.locationInRoom == xy },
        items.filter { it.locationInRoom == xy }
    )
}