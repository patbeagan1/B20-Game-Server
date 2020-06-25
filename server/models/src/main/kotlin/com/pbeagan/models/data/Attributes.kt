package com.pbeagan.models.data

data class Attributes(
    // misc
    val awareness: Int,
    val spirit: Int,
    // physical
    val strength: Int = 10,
    val precision: Int = 10,
    val agility: Int = 10,
    val durability: Int = 10,
    val endurance: Int = 10,
    // mental
    val presence: Int = 10,
    val persuasion: Int = 10,
    val cunning: Int = 10,
    val fortitude: Int = 10,
    val tenacity: Int = 10
) {
    companion object {
        fun random(): Attributes {
            val r20 = {
                val intRange = 1..20
                (1..3).map { intRange.random() }.sum() / 3
            }
            return Attributes(
                (0..5).random(),
                (0..5).random(),
                r20(),
                r20(),
                r20(),
                r20(),
                r20(),
                r20(),
                r20(),
                r20(),
                r20(),
                r20()
            )
        }
    }
}