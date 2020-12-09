package com.pbeagan.data

import com.pbeagan.models.FlagCombined
import com.pbeagan.models.createFlagSet

data class RoomData(
    val id: Int,
    val name: String,
    override val descriptionLook: String,
    val directions: List<RoomDirectionData>,
    val roomFlags: FlagCombined<RoomFlags> = createFlagSet(),
    val weather: Weather = Weather.CLEAR,
    val items: MutableCollection<ItemData> = mutableListOf()
) : Lookable