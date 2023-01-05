package dev.patbeagan.b20.domain

import com.pbeagan.contextual.ItemData
import com.pbeagan.contextual.Mob
import dev.patbeagan.b20.domain.terrain.type.Terrain

data class RoomTile(
    val terrain: Terrain,
    val mobs: List<Mob>,
    val items: List<ItemData>,
)