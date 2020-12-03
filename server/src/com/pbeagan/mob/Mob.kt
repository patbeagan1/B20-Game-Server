package com.pbeagan.mob

import com.pbeagan.BoundedValue
import com.pbeagan.ItemData
import com.pbeagan.MobBehavior
import com.pbeagan.VisibleBy
import com.pbeagan.actions.Action
import com.pbeagan.actions.Pass
import com.pbeagan.models.FlagCombined
import com.pbeagan.models.worldstate.Attr
import com.pbeagan.writer.IDforIOGenerator
import mobs
import rooms

data class Mob constructor(
    val name: String,
    val descriptionProvider: DescriptionProvider = object :
        DescriptionProvider {
        override fun describe(behavior: MobBehavior, action: Action) =
            behavior.descriptionDefault
    },
    var action: Action = Pass,
    var behavior: MobBehavior,

    var location: Int = 0,
    var visited: MutableSet<Int> = mutableSetOf(0),
    var visibleBy: FlagCombined<VisibleBy> = VisibleBy.defaultMob,

    var attr: Attr = Attr(),
    var baseAtkMelee: Int = 0,
    var baseAtkRanged: Int = 0,
    var baseAtkThrow: Int = 0,

    var armor: Int = 0,
    var dodge: Int = 0,
    var totalHearts: Int = 4,

    var items: MutableList<ItemData> = mutableListOf()
) {
    val idForIO: Int = IDforIOGenerator.get()
    var hearts by BoundedValue(totalHearts, 0..totalHearts)

    val description get() = descriptionProvider.describe(behavior, action)

    interface DescriptionProvider {
        fun describe(behavior: MobBehavior, action: Action): String
    }
}

fun Mob.currentRoom() = rooms[location]
fun Mob.currentRoomOtherMobs(list: List<Mob>) = list
    .filter { it.location == location && it != this }

fun Mob.getFirstVisibleMob(): Mob? = mobs
    .firstOrNull { it.location == location && it != this }

fun Mob.getRandomVisibleItem(): ItemData? =
    currentRoom()?.items?.takeIf { it.isNotEmpty() }?.random()

