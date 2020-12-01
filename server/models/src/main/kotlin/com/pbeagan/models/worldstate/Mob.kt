package com.pbeagan.models.worldstate


import com.google.gson.annotations.SerializedName

data class Mob(
    @SerializedName("name") val name: String, // Cloud
    @SerializedName("speed") val speed: Int, // 4
    @SerializedName("attr") val attr: Attr,

    @SerializedName("baseAtkMelee") val baseAtkMelee: Int, // 1
    @SerializedName("baseAtkRanged") val baseAtkRanged: Int, // 0
    @SerializedName("baseAtkThrow") val baseAtkThrow: Int, // 0
    @SerializedName("armor") val armor: Int, // 0
    @SerializedName("dodge") val dodge: Int, // 5
    @SerializedName("hearts") var hearts: Int, // 3

    @SerializedName("location") var location: Location,
    @SerializedName("items") val items: Items,
    @SerializedName("wieldedItems") val wieldedItems: Items
)