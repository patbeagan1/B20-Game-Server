package dev.patbeagan.b20.contextual

import dev.patbeagan.b20.WorldState
import dev.patbeagan.b20.domain.Exits
import dev.patbeagan.b20.domain.Lookable
import dev.patbeagan.b20.domain.RoomTile
import dev.patbeagan.b20.domain.flags.Lighting
import dev.patbeagan.b20.domain.flags.RoomFlags
import dev.patbeagan.b20.domain.terrain.parser.TerrainParser
import dev.patbeagan.b20.domain.terrain.type.Terrain
import dev.patbeagan.b20.domain.types.Direction
import dev.patbeagan.b20.domain.types.Weather
import dev.patbeagan.base.FlagSet
import dev.patbeagan.consolevision.types.CompressedPoint
import dev.patbeagan.consolevision.types.List2D

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

    fun WorldState.getLocation(xy: CompressedPoint) = RoomTile(
        terrain.at(xy.x, xy.y),
        mobs.filter { it.room == id && it.locationInRoom == xy },
        items.filter { it.locationInRoom == xy }
    )
}