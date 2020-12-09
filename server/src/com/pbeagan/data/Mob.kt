package com.pbeagan.data

import com.pbeagan.BoundedValue
import com.pbeagan.actions.Action
import com.pbeagan.actions.Pass
import com.pbeagan.data.AttackType.MELEE
import com.pbeagan.models.FlagCombined
import com.pbeagan.writer.IDforIOGenerator
import mobs
import rooms

class Mob constructor(
    val name: String,
    val descriptionProvider: DescriptionProvider = object :
        DescriptionProvider {
        override fun describe(behavior: MobBehavior, action: Action) =
            behavior.descriptionDefault
    },
    var action: Action = Pass,
    var behavior: MobBehavior,
    var mood: MobMood = MobMood.NEUTRAL,

    var location: Int = 0,
    var visited: MutableSet<Int> = mutableSetOf(0),
    var visibleBy: FlagCombined<VisibleBy> = VisibleBy.defaultMob,

    var stats: List<Stats> = listOf(StatsImpl.getInitial()),

    var armor: Int = 0,
    var dodge: Int = 0,

    var items: MutableList<ItemData> = mutableListOf()
) : Stats {
    override val baseAtkMelee: Int get() = stats.sumBy { it.baseAtkMelee }
    override val baseAtkRanged: Int get() = stats.sumBy { it.baseAtkRanged }
    override val baseAtkThrow: Int get() = stats.sumBy { it.baseAtkThrow }
    override val awareness: Int get() = stats.sumBy { it.awareness }
    override val spirit: Int get() = stats.sumBy { it.spirit }
    override val speed: Int get() = stats.sumBy { it.speed }
    override val presence: Int get() = stats.sumBy { it.presence }
    override val cunning: Int get() = stats.sumBy { it.cunning }
    override val persuasion: Int get() = stats.sumBy { it.persuasion }
    override val tenacity: Int get() = stats.sumBy { it.tenacity }
    override val fortitude: Int get() = stats.sumBy { it.fortitude }
    override val strength: Int get() = stats.sumBy { it.strength }
    override val agility: Int get() = stats.sumBy { it.agility }
    override val precision: Int get() = stats.sumBy { it.precision }
    override val endurance: Int get() = stats.sumBy { it.endurance }
    override val durability: Int get() = stats.sumBy { it.durability }
    override val totalHearts: Int get() = endurance.mod() + stats.sumBy { it.totalHearts }

    val description get() = descriptionProvider.describe(behavior, action)
    var preferredAttack: AttackType = MELEE
    val idForIO: Int = IDforIOGenerator.get()
    var hearts by BoundedValue(totalHearts, 0..totalHearts)


    interface DescriptionProvider {
        fun describe(behavior: MobBehavior, action: Action): String
    }
}

fun Int.mod() = (this - 10) / 2

fun Mob.currentRoom() =
    rooms[location]

fun Mob.adjacentOrCurrentRoom() =
    rooms[location]?.directions
        ?.map { it.destinationID }
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

