package com.pbeagan.models.worldstate


import com.google.gson.annotations.SerializedName

data class Items(
    @SerializedName("items") val items: ItemsX
)