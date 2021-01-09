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
    override val totalHearts: Int get() = effects.sumBy { it.totalHearts } + endurance.mod()
    override val visionBright: Int get() = effects.sumBy { it.visionBright }
    override val visionDim: Int get() = effects.sumBy { it.visionDim }
    override val visionDark: Int get() = effects.sumBy { it.visionDark }
    override val visionNone: Int get() = effects.sumBy { it.visionNone }

    var action: Action = Pass
    var actionMove: Move? = null
    val nameStyled = nameBase.style(colorForeground = TerminalColorStyle.Colors.Yellow)
    var preferredAttack: AttackType = MELEE
    val idForIO: Int = UniqueId.get()
    var hearts by BoundedValue(totalHearts, 0..totalHearts)
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
    }.let { Senses.checkLocalRange(this.locationInRoom.x, this.locationInRoom.y, it) }

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