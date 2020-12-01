package com.pbeagan.models.room


import com.google.gson.annotations.SerializedName

data class Entrance(
    @SerializedName("direction") val direction: String // west
)