package com.pbeagan.data

interface Stats {

    // Combat
    val baseAtkMelee: Int
    val baseAtkRanged: Int
    val baseAtkThrow: Int

    // Misc
    val awareness: Int
    val spirit: Int
    val speed: Int

    // Mental
    val presence: Int
    val cunning: Int
    val persuasion: Int
    val tenacity: Int
    val fortitude: Int

    // Physical
    val strength: Int
    val agility: Int
    val precision: Int
    val endurance: Int
    val durability: Int

    // Total hearts
    val totalHearts: Int
    val visionBright: Int
    val visionDim: Int
    val visionDark: Int
    val visionNone: Int
}

fun Int.mod() = (this - 10) / 2
