package com.pbeagan.models.worldstate


import com.google.gson.annotations.SerializedName

data class WorldState(
    @SerializedName("WorldState") val worldState: WorldStateX
)