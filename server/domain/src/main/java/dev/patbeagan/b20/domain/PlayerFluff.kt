package dev.patbeagan.b20.domain

data class PlayerFluff(
    val name: String,
    val description: String,
    val hometown: Int,
    val time: TimeData,
    val tmptime: TimeData,
    val weight: Int,
    val height: Int,
)