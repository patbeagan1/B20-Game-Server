package com.pbeagan.models

import com.pbeagan.models.worldstate.Mob

enum class MessageReceiver { Initiator, Retaliator, Room }

enum class ItemType { LIGHT, SCROLL, WAND, STAFF, WEAPON, FIREWEAPON, MISSILE, TREASURE, ARMOR, POTION, WORN, OTHER, TRASH, TRAP, CONTAINER, NOTE, DRINKCON, KEY, FOOD, MONEY, PEN, BOAT }

enum class WearPosition : Flag {
    FINGER, NECK, BODY, HEAD, LEGS, FEET, HANDS, ARMS, SHIELD, WAIST, WRIST, WIELD, BELT, BACK
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
    BLESSED;

    companion object {
        val default get() = flagSet(TAKEABLE)
    }
}

enum class ContainerState : Flag { CLOSEABLE, CLOSED, LOCKED }
interface Lookable {
    val descriptionLook: String
}

interface Examinable {
    val examine: String
}

enum class MagicType { FIRE, FROST, ELECTRIC, TIME, SPACE, BINDING, FORCE }
enum class MagicEnergy { POSITIVE, NEUTRAL, NEGATIVE }
enum class MagicMediums { AIR, WATER, EARTH, STONE, SAND }

data class ItemData(
    val id: Int,            /* Where in data-base               */
    val names: List<String>,/* Title of object :get etc.        */
    val descriptionOnExamination: String, /* extra descriptions     */
    val descriptionInRoom: String,            /* When in room                     */
    val handlerActivation: HandlerActivation? = null,
    val handlerDestruction: HandlerDestructible? = null,
    val itemFlags: FlagCombined<ItemFlags> = ItemFlags.default,/* Object information               */
    val visibleBy: FlagCombined<VisibleBy> = VisibleBy.defaultItem,  /* Which abilities in PC to change  */
    val affectedByMagicPossible: FlagCombined<AffectedByMagic> = AffectedByMagic.defaultItem,  /* Which abilities in PC to change  */
    val affectedByMagicCurrent: FlagCombined<AffectedByMagic> = flagSet(),  /* Which abilities in PC to change  */
    val contains: ItemData? = null  /* Contains objects                 */
) {
    interface HandlerActivation {
        fun activate()
        val descriptionWhileActive: String       /* when worn/carry/in cont.         */
        val descriptionOnActivation: String      /* What to write when used          */
    }

    interface HandlerDestructible {
        fun destroy()
        val descriptionOnDestruction: String
    }
}
enum class RoomFlags : Flag { DARK, DEATH, NO_MOB, INDOORS, LAWFUL, NEUTRAL, CHAOTIC, NO_MAGIC, TUNNEL, PRIVATE }

enum class Direction { NORTH, EAST, SOUTH, WEST, UP, DOWN }

enum class Biome {
    INSIDE, URBAN, FIELD, FOREST, HILLS, MOUNTAINS, WATER, RAIN_FOREST,
    SWAMP, SAVANNAH, SHRUBLAND, TAIGA, DESERT, PLAINS, TUNDRA
}

enum class ExitCondition : Flag { REQ_KEY }

data class RoomDirectionData(
    val direction: Direction,
    val destinationID: Int,
    val preview: String,
    val exitConditions: FlagCombined<ExitCondition> = flagSet()
)


data class RoomData(
    val id: Int,
    val name: String,                  /* Rooms name 'You are ...'           */
    override val descriptionLook: String,
    val directions: List<RoomDirectionData>, /* Directions           */
    val roomFlags: FlagCombined<RoomFlags> = flagSet(),          /* DEATH,DARK ... etc                 */
    val weather: Weather = Weather.CLEAR,                 /* Number of lightsources in room     */
    val items: MutableCollection<ItemData> = mutableListOf()   /* List of items in room              */
) : Lookable

enum class AffectedByMagic : Flag {
    INVISIBLE, SLEEP, CURSE, FIRE, ELECTRIC, FROST, PARALYSIS;

    companion object {
        val defaultItem get() = flagSet(INVISIBLE)
    }
}

enum class Impediments : Flag {  //Lowered Abilities
    Clumsy, Drained, Enfeebled, Stupefied,     //Senses
    Blinded, Dazzled, Deafened
}

enum class SneakLevel { HIDDEN, UNDETECTED, SUSPICIOUS, DETECTED }
enum class VisibleBy : Flag {
    SIGHT,
    SOUND,
    SENSE_LIFE,
    DETECT_EVIL,
    DETECT_INVISIBLE,
    DETECT_MAGIC;

    companion object {
        val defaultMob: FlagCombined<VisibleBy>
            get() = flagSet(SIGHT, SOUND, SENSE_LIFE)
        val defaultItem: FlagCombined<VisibleBy>
            get() = flagSet(SIGHT)
    }
}

enum class DeathStages {

    //Death and Dying
    Doomed, Dying, Unconscious, Wounded, HEALTHY
}

enum class Genus { DRAGON, CANID, FELINE, TROLL, GOBLIN, HUMAN }

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
    val birth: Long    /* This represents the characters age                */
)

data class PlayerFluff(
    val name: String,            /* PC / NPC s name (kill ...  )         */
    val description: String,  /* Extra descriptions                   */
    val hometown: Int,       /* PC s Hometown (zone)                 */
    val time: TimeData, /* PC s AGE in days                  */
    val tmptime: TimeData, /* PC s AGE in days, modified     */
    val weight: Int,       /* PC / NPC s weight                    */
    val height: Int       /* PC / NPC s height                    */
);

enum class Weather {
    CLEAR, CLOUDY, RAINY, STORMING
}

enum class Season {
    SUMMER, FALL, WINTER, SPRING
}