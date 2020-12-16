package com.pbeagan.data

import com.pbeagan.util.BoundedValue
import com.pbeagan.actions.Action
import com.pbeagan.actions.Pass
import com.pbeagan.ancestry.Ancestry
import com.pbeagan.ancestry.Human
import com.pbeagan.data.AttackType.MELEE
import com.pbeagan.data.Effect.Type.ANCESTRY
import com.pbeagan.data.MobBehavior.WAITING
import com.pbeagan.data.MobMood.NEUTRAL
import com.pbeagan.util.FlagCombined
import com.pbeagan.writer.UniqueId
import com.pbeagan.writer.Reader
import com.pbeagan.writer.Writer

class Mob constructor(
    val name: String,
    val description: Description,
    var action: Action = Pass,
    var behavior: MobBehavior = WAITING,
    var mood: MobMood = NEUTRAL,

    var isPlayer: Boolean = false,

    var armor: Int = 0,
    var dodge: Int = 0,

    var location: Int = 0,
    var locationInRoom: Pair<Int, Int> = 0 to 0,

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

    var preferredAttack: AttackType = MELEE
    val idForIO: Int = UniqueId.get()
    var hearts by BoundedValue(totalHearts, 0..totalHearts)
    val ancestry by lazy { effects.firstOrNull { it is Ancestry } as? Ancestry }
    val party: MutableSet<Mob> = mutableSetOf()
    val allies: MutableSet<Mob> = mutableSetOf()

    interface Description {
        fun onExamine(ancestry: Ancestry): String = "A fine example of a ${ancestry::class.java.simpleName}"
        fun onLook(behavior: MobBehavior) = behavior.descriptionDefault
    }

    fun addEffect(writer: Writer, effect: Effect) {
        val sayToRoom = writer.sayToRoomOf(this)
        when {
            effect.type == ANCESTRY && effects.any { it.type == ANCESTRY } ->
                sayToRoom.error("Only one Ancestry allowed.")
            else -> effects = effects + effect
        }
    }

    fun getAction(reader: Reader): Action? = ancestry?.decide(this, this.behavior)
}