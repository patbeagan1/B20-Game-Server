package com.pbeagan.models.room


import com.google.gson.annotations.SerializedName

data class RoomX(
    @SerializedName("description") val description: String, // Two paces out of the house. There is a long way to go!
    @SerializedName("entrance") val entrance: Entrance,
    @SerializedName("exit") val exit: List<Exit>,
    @SerializedName("items") val items: List<Item>,
    @SerializedName("mobs") val mobs: List<Mob>
)