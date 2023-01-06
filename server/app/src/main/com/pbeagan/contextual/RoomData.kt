package com.pbeagan.contextual

import com.pbeagan.WorldState
import com.pbeagan.consolevision.Coord

import dev.patbeagan.base.FlagSet
import com.pbeagan.consolevision.List2D
import dev.patbeagan.b20.domain.Exits
import dev.patbeagan.b20.domain.Lookable
import dev.patbeagan.b20.domain.RoomTile
import dev.patbeagan.b20.domain.flags.Lighting
import dev.patbeagan.b20.domain.flags.RoomFlags
import dev.patbeagan.b20.domain.terrain.type.Terrain
import dev.patbeagan.b20.domain.terrain.parser.TerrainParser
import dev.patbeagan.b20.domain.types.Direction
import dev.patbeagan.b20.domain.types.Weather

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

    fun WorldState.getLocation(xy: Coord) = RoomTile(
        terrain.at(xy.x, xy.y),
        mobs.filter { it.room == id && it.locationInRoom == xy },
        items.filter { it.locationInRoom == xy }
    )
}