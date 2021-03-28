package com.pbeagan.data

import com.pbeagan.actions.Action
import com.pbeagan.actions.Drop
import com.pbeagan.actions.Move
import com.pbeagan.actions.Pass
import com.pbeagan.ancestry.Ancestry
import com.pbeagan.ancestry.Human
import com.pbeagan.data.AttackType.MELEE
import com.pbeagan.data.Effect.Type.ANCESTRY
import com.pbeagan.data.Lighting.BRIGHT
import com.pbeagan.data.Lighting.DARK
import com.pbeagan.data.Lighting.DIM
import com.pbeagan.data.Lighting.NONE
import com.pbeagan.data.MobBehavior.WAITING
import com.pbeagan.data.MobMood.NEUTRAL
import com.pbeagan.util.BoundedValue
import com.pbeagan.util.Coord
import com.pbeagan.util.FlagCombined
import com.pbeagan.util.Senses
import com.pbeagan.util.coord
import com.pbeagan.writer.Reader
import com.pbeagan.writer.TerminalColorStyle
import com.pbeagan.writer.TerminalColorStyle.style
import com.pbeagan.writer.UniqueId
import com.pbeagan.writer.Writer

class Mob constructor(
    val nameBase: String,
    val description: Description,

    var behavior: MobBehavior = WAITING,
    var mood: MobMood = NEUTRAL,

    var isPlayer: Boolean = false,

    var armor: Int = 0,
    var dodge: Int = 0,

    var location: Int = 0,
    override var locationInRoom: Coord,

    var visited: MutableSet<Int> = mutableSetOf(0),
    var visibleBy: FlagCombined<VisibleBy> = VisibleBy.defaultMob,

    var effects: List<Effect> = listOf(Human()),
    var items: MutableList<ItemData> = mutableListOf()
) : Stats, HasLocation {
    fun List<Effect>.sumBy(valueContainerTransform: (Effect) -> ValueContainer<Int>) =
        this.sumBy(selector = { valueContainerTransform(it).value })


    override val baseAtkMelee: CombatValue get() = CombatValue(effects.sumBy { it.baseAtkMelee })
    override val baseAtkRanged: CombatValue get() = CombatValue(effects.sumBy { it.baseAtkRanged })
    override val baseAtkThrow: CombatValue get() = CombatValue(effects.sumBy { it.baseAtkThrow })
    override val awareness: MiscValue get() = MiscValue(effects.sumBy { it.awareness })
    override val spirit: MiscValue get() = MiscValue(effects.sumBy { it.spirit })
    override val speed: MiscValue get() = MiscValue(effects.sumBy { it.speed })
    override val presence: MentalValue get() = MentalValue(effects.sumBy { it.presence })
    override val cunning: MentalValue get() = MentalValue(effects.sumBy { it.cunning })
    override val persuasion: MentalValue get() = MentalValue(effects.sumBy { it.persuasion })
    override val tenacity: MentalValue get() = MentalValue(effects.sumBy { it.tenacity })
    override val fortitude: MentalValue get() = MentalValue(effects.sumBy { it.fortitude })
    override val strength: PhysicalValue get() = PhysicalValue(effects.sumBy { it.strength })
    override val agility: PhysicalValue get() = PhysicalValue(effects.sumBy { it.agility })
    override val precision: PhysicalValue get() = PhysicalValue(effects.sumBy { it.precision })
    override val endurance: PhysicalValue get() = PhysicalValue(effects.sumBy { it.endurance })
    override val durability: PhysicalValue get() = PhysicalValue(effects.sumBy { it.durability })
    override val totalHearts: HealthValue get() = HealthValue(effects.sumBy { it.totalHearts }) + endurance.mod()
    override val visionBright: VisionValue get() = VisionValue(effects.sumBy { it.visionBright })
    override val visionDim: VisionValue get() = VisionValue(effects.sumBy { it.visionDim })
    override val visionDark: VisionValue get() = VisionValue(effects.sumBy { it.visionDark })
    override val visionNone: VisionValue get() = VisionValue(effects.sumBy { it.visionNone })

    var action: Action = Pass
    var actionMove: Move? = null
    val nameStyled = nameBase.style(colorForeground = TerminalColorStyle.Colors.Yellow)
    var preferredAttack: AttackType = MELEE
    val idForIO: Int = UniqueId.get()
    var hearts by BoundedValue(totalHearts.value, 0..totalHearts.value)
    val ancestry by lazy { effects.firstOrNull { it is Ancestry } as? Ancestry }
    val party: MutableSet<Mob> = mutableSetOf()

    interface Description {
        fun onExamine(ancestry: Ancestry): String = "A fine example of a ${ancestry::class.java.simpleName}"
        fun onLook(behavior: MobBehavior) = behavior.descriptionDefault
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

        location = 0
        locationInRoom = 0 coord 0
    }
}