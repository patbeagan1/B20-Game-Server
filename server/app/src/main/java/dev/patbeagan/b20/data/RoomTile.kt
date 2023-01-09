package dev.patbeagan.b20.domain

import dev.patbeagan.b20.contextual.ItemData
import dev.patbeagan.b20.contextual.Mob
import dev.patbeagan.b20.domain.terrain.type.Terrain

data class RoomTile(
    val terrain: Terrain,
    val mobs: List<Mob>,
    val items: List<ItemData>,
)