package com.pbeagan.models.room


import com.google.gson.annotations.SerializedName

data class Item(
    @SerializedName("name") val name: String // baseball
)