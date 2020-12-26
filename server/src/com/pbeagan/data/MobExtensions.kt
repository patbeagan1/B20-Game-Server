package com.pbeagan.data

import com.pbeagan.demo.SampleData
import com.pbeagan.util.startsWith
import mobs
import rooms

fun Mob.currentRoom() =
    rooms[location]

fun Mob.adjacentOrCurrentRoom() =
    rooms[location]?.exits?.asList
        ?.map { it.second.destinationID }
        ?.let { it + location }
        ?.map { rooms[it] }

fun Mob.currentRoomOtherMobs(list: List<Mob>) = list
    .filter { it.location == location && it != this }

fun Mob.currentRoomOtherMobsAndSelf(list: List<Mob>) = list
    .filter { it.location == location }

fun Mob.getFirstVisibleMob(): Mob? = mobs
    .firstOrNull { it.location == location && it != this }

fun Mob.getRandomVisibleItem(): ItemData? =
    currentRoom()?.items?.takeIf { it.isNotEmpty() }?.random()

fun Mob.target(targetName: String): Mob? = this
    .currentRoomOtherMobs(SampleData.mobs)
    .firstOrNull { it.nameBase.startsWith(targetName) }

fun Mob.formatHP() = "${this.nameStyled}(${this.hearts} hp)"
