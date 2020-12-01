package com.pbeagan.models.room


import com.google.gson.annotations.SerializedName

data class Exit(
    @SerializedName("direction") val direction: String, // east
    @SerializedName("to") val to: String // Haunted House
)