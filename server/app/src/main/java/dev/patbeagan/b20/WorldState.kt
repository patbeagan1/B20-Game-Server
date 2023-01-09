package dev.patbeagan.b20

import dev.patbeagan.b20.contextual.ItemData
import dev.patbeagan.b20.contextual.Mob
import dev.patbeagan.b20.contextual.RoomData
import dev.patbeagan.b20.util.commonPrefixWithIgnoreCase

class WorldState(
    val mobs: List<Mob>,
    val rooms: Map<Int, RoomData>,
){

    fun Mob.currentRoom() =
        rooms[room]

    fun Mob.adjacentOrCurrentRoom() =
        rooms[room]?.exits?.asList
            ?.map { it.second.destinationID }
            ?.let { it + room }
            ?.map { rooms[it] }

    fun Mob.currentRoomOtherMobs(list: List<Mob>) = list
        .filter { it.room == room && it != this }

    fun Mob.currentRoomOtherMobsAndSelf(list: List<Mob>) = list
        .filter { it.room == room }

    fun Mob.getFirstVisibleMob(): Mob? = mobs
        .firstOrNull { it.room == room && it != this }

    fun Mob.getRandomVisibleItem(): ItemData? =
        currentRoom()?.items?.takeIf { it.isNotEmpty() }?.random()

    fun Mob.target(targetName: String): Mob? = this
        .currentRoomOtherMobs(mobs)
        .firstOrNull { it.nameBase.commonPrefixWithIgnoreCase(targetName) }

}