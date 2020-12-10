package com.pbeagan.data

import com.pbeagan.BoundedValue
import com.pbeagan.SampleData
import com.pbeagan.actions.Action
import com.pbeagan.actions.Pass
import com.pbeagan.data.AttackType.MELEE
import com.pbeagan.data.Effect.Type.ANCESTRY
import com.pbeagan.earlyMatches
import com.pbeagan.models.FlagCombined
import com.pbeagan.writer.IDforIOGenerator
import com.pbeagan.writer.Writer
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

    var armor: Int = 0,
    var dodge: Int = 0,

    var location: Int = 0,
    var visited: MutableSet<Int> = mutableSetOf(0),
    var visibleBy: FlagCombined<VisibleBy> = VisibleBy.defaultMob,

    var effects: List<Effect> = listOf(Human()),
    var items: MutableList<ItemData> = mutableListOf()
) : Stats {
    override val baseAtkMelee: Int get() = effects.sumBy { it.baseAtkMelee }
    override val baseAtkRanged: Int get() = effects.sumBy { it.baseAtkRanged }
    override val baseAtkThrow: Int get() = effects.sumBy { it.baseAtkThrow }
    override val awareness: Int get() = effects.sumBy { it.awareness }
    override val spirit: Int get() = effects.sumBy { it.spirit }
    override val speed: Int get() = effects.sumBy { it.speed }
    override val presence: Int get() = effects.sumBy { it.presence }
    override val cunning: Int get() = effects.sumBy { it.cunning }
    override val persuasion: Int get() = effects.sumBy { it.persuasion }
    override val tenacity: Int get() = effects.sumBy { it.tenacity }
    override val fortitude: Int get() = effects.sumBy { it.fortitude }
    override val strength: Int get() = effects.sumBy { it.strength }
    override val agility: Int get() = effects.sumBy { it.agility }
    override val precision: Int get() = effects.sumBy { it.precision }
    override val endurance: Int get() = effects.sumBy { it.endurance }
    override val durability: Int get() = effects.sumBy { it.durability }
    override val totalHearts: Int get() = endurance.mod() + effects.sumBy { it.totalHearts }

    val description get() = descriptionProvider.describe(behavior, action)
    var preferredAttack: AttackType = MELEE
    val idForIO: Int = IDforIOGenerator.get()
    var hearts by BoundedValue(totalHearts, 0..totalHearts)


    interface DescriptionProvider {
        fun describe(behavior: MobBehavior, action: Action): String
    }

    fun addEffect(writer: Writer, effect: Effect) {
        val sayToRoom = writer.sayToRoomOf(this)
        when {
            effect.type == ANCESTRY && effects.any { it.type == ANCESTRY } ->
                sayToRoom.error("Only one Ancestry allowed.")
            else -> effects = effects + effect
        }
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

fun Mob.target(targetName: String): Mob? = this
    .currentRoomOtherMobs(SampleData.mobs)
    .firstOrNull { it.name.earlyMatches(targetName) }

