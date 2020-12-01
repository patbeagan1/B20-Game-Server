package com.pbeagan.models.worldstate


import com.google.gson.annotations.SerializedName

data class Location(
    @SerializedName("x") val x: Double, // 98.0
    @SerializedName("y") val y: Double // 34.0
)