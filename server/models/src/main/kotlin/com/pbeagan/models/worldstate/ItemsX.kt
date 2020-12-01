package com.pbeagan.models.worldstate


import com.google.gson.annotations.SerializedName

data class ItemsX(
    @SerializedName("encumbrance") val encumbrance: Double, // 0
    val type_flag: Byte = 0,
    val wear_flags: Int = 0,
    val extra_flags: Int = 0,
    val weight: Int = 0,
    val cost: Int = 0,
    val cost_per_day: Int = 0,
    val timer: Int = 0
)