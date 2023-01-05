package com.pbeagan.contextual

import com.pbeagan.demo.SampleData
import com.pbeagan.contextual.Mob
import mobs
import rooms

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
    .currentRoomOtherMobs(SampleData.mobs)
    .firstOrNull { it.nameBase.startsWith(targetName) }

fun Mob.formatHP() = "${this.nameStyled}(${this.hearts} hp)"
