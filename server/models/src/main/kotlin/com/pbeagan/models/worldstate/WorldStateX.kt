package com.pbeagan.models.worldstate


import com.google.gson.annotations.SerializedName
import com.pbeagan.models.room.Room

data class WorldStateX(
    @SerializedName("rooms") val rooms: Map<String, Room>,
    @SerializedName("players") val mobs: List<Mob>
)