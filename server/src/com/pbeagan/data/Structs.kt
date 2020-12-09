package com.pbeagan.data

import com.pbeagan.models.Flag

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

enum class ContainerState : Flag {
    CLOSEABLE,
    CLOSED,
    LOCKED
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
    INCAPACITATED,
    STUNNED,
    SLEEPING,
    RESTING,
    SITTING,
    FIGHTING,
    STANDING
}

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