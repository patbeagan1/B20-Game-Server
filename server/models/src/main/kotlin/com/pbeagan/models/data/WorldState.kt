package com.pbeagan.models.data

data class WorldState(
    val players: List<Player>,
    val npcs: List<NPC>,
    val dungeons: List<Dungeon>,
    val treasures: List<Treasure>
)

class NPC

class Dungeon

class Treasure
