package com.pbeagan

import com.pbeagan.mob.Mob
import com.pbeagan.models.Flag
import com.pbeagan.models.FlagCombined
import com.pbeagan.models.createFlagSet

enum class MessageReceiver {
    INITIATOR,
    RETALIATOR,
    ROOM
}

enum class ItemType {
    LIGHT,
    SCROLL,
    WAND,
    STAFF,
    WEAPON,
    FIREWEAPON,
    MISSILE,
    TREASURE,
    ARMOR,
    POTION,
    WORN,
    OTHER,
    TRASH,
    TRAP,
    CONTAINER,
    NOTE,
    DRINKCON,
    KEY,
    FOOD,
    MONEY,
    PEN,
    BOAT
}

enum class WearPosition : Flag {
    FINGER,
    NECK,
    BODY,
    HEAD,
    LEGS,
    FEET,
    HANDS,
    ARMS,
    SHIELD,
    WAIST,
    WRIST,
    WIELD,
    BELT,
    BACK
}

enum class ItemFlags : Flag {
    GLOW,
    HUM,
    INVISIBLE,
    MAGIC,
    TAKEABLE,
    UNDROPPABLE,
    LOCKED,
    KEY,
    WIELDABLE,
    CONSUMABLE,
    BLESSED;

    companion object {
        val default
            get() = createFlagSet(
                TAKEABLE
            )
    }
}

enum class ContainerState : Flag {
    CLOSEABLE,
    CLOSED,
    LOCKED
}

interface Lookable {
    val descriptionLook: String
}

interface Examinable {
    val examine: String
}

enum class MagicType {
    FIRE,
    FROST,
    ELECTRIC,
    TIME,
    SPACE,
    BINDING,
    FORCE
}

enum class MagicEnergy {
    POSITIVE,
    NEUTRAL,
    NEGATIVE
}

enum class MagicMediums {
    AIR,
    WATER,
    EARTH,
    STONE,
    SAND
}

data class ItemData(
    val id: Int,
    val names: List<String>,
    val descriptionOnExamination: String,
    val descriptionInRoom: String
) {
    val affectedByMagicPossible: FlagCombined<AffectedByMagic> = AffectedByMagic.defaultItem
    val affectedByMagicCurrent: FlagCombined<AffectedByMagic> = createFlagSet()
    val containsInnerItem: ItemData? = null
    val itemFlags: FlagCombined<ItemFlags> = ItemFlags.default
    val visibleBy: FlagCombined<VisibleBy> = VisibleBy.defaultItem
    val flagHandlers = mutableMapOf<ItemFlags, FlagHandler?>()

    fun setItemFlags(flags: ItemFlags, handler: FlagHandler? = null) {
        itemFlags.add(flags)
        if (handler != null) {
            flagHandlers[flags] = handler
        }
    }

    fun nameMatches(item: String) =
        names.any { item.earlyMatches(it) }

    interface FlagHandler {
        fun invoke(self: Mob) = Unit
        val descriptionOnActivation: String? get() = null
        val descriptionOnDuration: String? get() = null
        val descriptionOnCompletion: String? get() = null
    }
}

enum class RoomFlags : Flag {
    DARK,
    DEATH,
    NO_MOB,
    INDOORS,
    LAWFUL,
    NEUTRAL,
    CHAOTIC,
    NO_MAGIC,
    TUNNEL,
    PRIVATE
}

enum class Direction {
    NORTH,
    EAST,
    SOUTH,
    WEST,
    UP,
    DOWN
}

enum class Biome {
    INSIDE,
    URBAN,
    FIELD,
    FOREST,
    HILLS,
    MOUNTAINS,
    WATER,
    RAIN_FOREST,
    SWAMP,
    SAVANNAH,
    SHRUBLAND,
    TAIGA,
    DESERT,
    PLAINS,
    TUNDRA
}

enum class ExitCondition : Flag { REQ_KEY }

data class RoomDirectionData(
    val direction: Direction,
    val destinationID: Int,
    val preview: String,
    val exitConditions: FlagCombined<ExitCondition> = createFlagSet()
)


data class RoomData(
    val id: Int,
    val name: String,
    override val descriptionLook: String,
    val directions: List<RoomDirectionData>,
    val roomFlags: FlagCombined<RoomFlags> = createFlagSet(),
    val weather: Weather = Weather.CLEAR,
    val items: MutableCollection<ItemData> = mutableListOf()
) : Lookable

enum class AffectedByMagic : Flag {
    INVISIBLE,
    SLEEP,
    CURSE,
    FIRE,
    ELECTRIC,
    FROST,
    PARALYSIS;

    companion object {
        val defaultItem
            get() = createFlagSet(
                INVISIBLE
            )
    }
}

enum class Impediments : Flag {  //Lowered Abilities
    Clumsy,
    Drained,
    Enfeebled,
    Stupefied,

    //Senses
    Blinded,
    Dazzled,
    Deafened
}

enum class SneakLevel {
    HIDDEN,
    UNDETECTED,
    SUSPICIOUS,
    DETECTED
}

enum class VisibleBy : Flag {
    SIGHT,
    SOUND,
    SENSE_LIFE,
    DETECT_EVIL,
    DETECT_INVISIBLE,
    DETECT_MAGIC;

    companion object {
        val defaultMob: FlagCombined<VisibleBy>
            get() = createFlagSet(
                SIGHT,
                SOUND,
                SENSE_LIFE
            )
        val defaultItem: FlagCombined<VisibleBy>
            get() = createFlagSet(SIGHT)
    }
}

enum class DeathStages {
    DOOMED,
    DYING,
    UNCONSCIOUS,
    WOUNDED,
    HEALTHY
}

enum class Genus {
    DRAGON,
    CANID,
    FELINE,
    TROLL,
    GOBLIN,
    HUMAN
}

enum class Position {
    DEAD,
    MORTALLYW,
    INCAPACITATED,
    STUNNED,
    SLEEPING,
    RESTING,
    SITTING,
    FIGHTING,
    STANDING
}

enum class MobBehavior(val descriptionDefault: String) : Flag {
    PLAYER("Has an intelligence unlike other creatures in this world."),
    HELPFUL("Seems to want to help."),
    IMMOBILE("It's rooted in place!"),
    LOOTER("It scours the area for items of value"),
    AGGRESSIVE("It looks angry..."),
    WANDERER("It is just passing through."),
    FEARFUL("It wishes it was someplace else.")
}

data class TimeData(
    val birth: Long
)

data class PlayerFluff(
    val name: String,
    val description: String,
    val hometown: Int,
    val time: TimeData,
    val tmptime: TimeData,
    val weight: Int,
    val height: Int
);

enum class Weather {
    CLEAR,
    CLOUDY,
    RAINY,
    STORMING
}

enum class Season {
    SUMMER,
    FALL,
    WINTER,
    SPRING
}