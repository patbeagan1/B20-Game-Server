package com.pbeagan.models.worldstate


import com.google.gson.annotations.SerializedName

data class Attr(

    // Misc
    @SerializedName("awareness") val awareness: Int = 4, // 4
    @SerializedName("spirit") val spirit: Int = 0, // 0
    var speed: Int = 4,

    // Mental
    @SerializedName("presence") val presence: Int = 10, // 3
    @SerializedName("cunning") val cunning: Int = 10, // 7
    @SerializedName("persuasion") val persuasion: Int = 10, // 11
    @SerializedName("tenacity") val tenacity: Int = 10, // 11
    @SerializedName("fortitude") val fortitude: Int = 10, // 14

    // Physical
    @SerializedName("strength") val strength: Int = 10, // 10
    @SerializedName("agility") val agility: Int = 10, // 10
    @SerializedName("precision") val precision: Int = 10, // 10
    @SerializedName("endurance") val endurance: Int = 10, // 10
    @SerializedName("durability") val durability: Int = 10 // 6
)
