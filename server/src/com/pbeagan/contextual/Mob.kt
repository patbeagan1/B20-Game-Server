package com.pbeagan.contextual

import com.pbeagan.contextual.actions.type.Action
import com.pbeagan.contextual.actions.Drop
import com.pbeagan.contextual.actions.Move
import com.pbeagan.contextual.actions.Pass
import com.pbeagan.contextual.ancestry.type.Ancestry
import com.pbeagan.contextual.ancestry.Human
import com.pbeagan.consolevision.Coord
import dev.patbeagan.b20.domain.types.AttackType.MELEE
import dev.patbeagan.b20.domain.Effect.Type.ANCESTRY
import dev.patbeagan.b20.domain.flags.Lighting.BRIGHT
import dev.patbeagan.b20.domain.flags.Lighting.DARK
import dev.patbeagan.b20.domain.flags.Lighting.DIM
import dev.patbeagan.b20.domain.flags.Lighting.NONE
import dev.patbeagan.base.BoundedValue

import dev.patbeagan.base.FlagSet
import com.pbeagan.util.Senses

import com.pbeagan.data.reader.Reader
import com.pbeagan.consolevision.TerminalColorStyle
import com.pbeagan.consolevision.TerminalColorStyle.style
import com.pbeagan.consolevision.coord
import dev.patbeagan.b20.domain.flags.Lighting
import dev.patbeagan.b20.domain.flags.VisibleBy
import dev.patbeagan.b20.domain.types.AttackType
import dev.patbeagan.b20.domain.stats.AttackValue
import dev.patbeagan.b20.domain.stats.DefenseValue
import dev.patbeagan.b20.domain.stats.HealthValue
import dev.patbeagan.b20.domain.stats.MentalValue
import dev.patbeagan.b20.domain.stats.MiscValue
import dev.patbeagan.b20.domain.stats.PhysicalValue
import dev.patbeagan.b20.domain.stats.Stats
import dev.patbeagan.b20.domain.stats.VisionValue
import dev.patbeagan.base.UniqueId
import com.pbeagan.data.writer.Writer
import dev.patbeagan.b20.domain.Effect
import dev.patbeagan.b20.domain.HasLocation

class Mob constructor(
    val nameBase: String,
    val description: com.pbeagan.contextual.Mob.Description,

    var behavior: com.pbeagan.contextual.Mob.MobBehavior = com.pbeagan.contextual.Mob.MobBehavior.WAITING,
    var mood: com.pbeagan.contextual.Mob.MobMood = com.pbeagan.contextual.Mob.MobMood.NEUTRAL,

    var isPlayer: Boolean = false,

    var armor: DefenseValue = DefenseValue(0),
    var dodge: DefenseValue = DefenseValue(0),

    var room: Int = 0,
    override var locationInRoom: Coord,

    var visited: MutableSet<Int> = mutableSetOf(0),
    var visibleBy: FlagSet<VisibleBy> = VisibleBy.defaultMob,

    var effects: List<Effect> = listOf(Human()),
    var items: MutableList<ItemData> = mutableListOf(),
) : Stats, HasLocation {

    override val baseAtkMelee: AttackValue get() = AttackValue(effects.sumOf { it.baseAtkMelee.value })
    override val baseAtkRanged: AttackValue get() = AttackValue(effects.sumOf { it.baseAtkRanged.value })
    override val baseAtkThrow: AttackValue get() = AttackValue(effects.sumOf { it.baseAtkThrow.value })
    override val awareness: MiscValue get() = MiscValue(effects.sumOf { it.awareness.value })
    override val spirit: MiscValue get() = MiscValue(effects.sumOf { it.spirit.value })
    override val speed: MiscValue get() = MiscValue(effects.sumOf { it.speed.value })
    override val presence: MentalValue get() = MentalValue(effects.sumOf { it.presence.value })
    override val cunning: MentalValue get() = MentalValue(effects.sumOf { it.cunning.value })
    override val persuasion: MentalValue get() = MentalValue(effects.sumOf { it.persuasion.value })
    override val tenacity: MentalValue get() = MentalValue(effects.sumOf { it.tenacity.value })
    override val fortitude: MentalValue get() = MentalValue(effects.sumOf { it.fortitude.value })
    override val strength: PhysicalValue get() = PhysicalValue(effects.sumOf { it.strength.value })
    override val agility: PhysicalValue get() = PhysicalValue(effects.sumOf { it.agility.value })
    override val precision: PhysicalValue get() = PhysicalValue(effects.sumOf { it.precision.value })
    override val endurance: PhysicalValue get() = PhysicalValue(effects.sumOf { it.endurance.value })
    override val durability: PhysicalValue get() = PhysicalValue(effects.sumOf { it.durability.value })
    override val totalHearts: HealthValue get() = HealthValue(effects.sumOf { it.totalHearts.value }) + endurance.mod()
    override val visionBright: VisionValue get() = VisionValue(effects.sumOf { it.visionBright.value })
    override val visionDim: VisionValue get() = VisionValue(effects.sumOf { it.visionDim.value })
    override val visionDark: VisionValue get() = VisionValue(effects.sumOf { it.visionDark.value })
    override val visionNone: VisionValue get() = VisionValue(effects.sumOf { it.visionNone.value })

    var action: Action = Pass
    var actionMove: Move? = null
    val nameStyled = nameBase.style(colorForeground = TerminalColorStyle.Colors.Yellow)
    var preferredAttack: AttackType = MELEE
    val idForIO: Int = UniqueId.get()
    var hearts by BoundedValue(totalHearts.value, 0..totalHearts.value)
    val ancestry by lazy { effects.firstOrNull { it is Ancestry } as? Ancestry }
    val party: MutableSet<com.pbeagan.contextual.Mob> = mutableSetOf()

    interface Description {
        fun onExamine(ancestry: Ancestry): String =
            "A fine example of a ${ancestry::class.java.simpleName}"

        fun onLook(behavior: com.pbeagan.contextual.Mob.MobBehavior) = behavior.descriptionDefault
    }

    fun vision(lighting: Lighting) = when (lighting) {
        BRIGHT -> visionBright
        DIM -> visionDim
        DARK -> visionDark
        NONE -> visionNone
    }.let { Senses.checkLocalRange(this.locationInRoom.x, this.locationInRoom.y, it.value) }

    fun range(r: Int) = Senses.checkLocalRange(
        this.locationInRoom.x,
        this.locationInRoom.y,
        r
    )

    fun addEffect(writer: Writer, effect: Effect) {
        val sayToRoom = writer.sayToRoomOf(this)
        when {
            effect.type == ANCESTRY && effects.any { it.type == ANCESTRY } ->
                sayToRoom.error("Only one Ancestry allowed.")

            else -> {
                effects = effects + effect
                sayToRoom.info(effect.descriptionActivation)
            }
        }
    }

    fun getAction(reader: Reader): Action? = ancestry?.decide(this, this.behavior)

    fun die(writer: Writer) {
        items.map { itemData ->
            Drop(itemData).also { it.writer = writer }
        }.forEach { it.invoke(this) }

        Drop(
            ItemData(
                this.idForIO,
                listOf("Body", this.nameBase),
                "${this.nameStyled} died here. RIP ${this.nameStyled}.",
                "There is a body here.",
                0 coord 0
            )
        ).also { it.writer = writer }(this)

        room = 0
        locationInRoom = 0 coord 0
    }


    enum class MobBehavior(val descriptionDefault: String) {
        HELPFUL("Seems to want to help."),
        IMMOBILE("It's rooted in place!"),
        LOOTER("It scours the area for items of value"),
        AGGRESSIVE("It looks angry..."),
        WANDERER("It is just passing through."),
        FLEE("It wishes it was someplace else."),
        SLEEPING("Is is fast asleep."),
        FOLLOW("It is following something."),
        WAITING("It is waiting for something to happen.")
    }

    enum class MobMood {
        ANGRY,
        PEACEFUL,
        HAPPY,
        NEUTRAL,
        FEARFUL,
    }
}