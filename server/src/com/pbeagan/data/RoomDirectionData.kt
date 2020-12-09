package com.pbeagan.data

import com.pbeagan.models.FlagCombined
import com.pbeagan.models.createFlagSet

data class RoomDirectionData(
    val direction: Direction,
    val destinationID: Int,
    val preview: String,
    val exitConditions: FlagCombined<ExitCondition> = createFlagSet()
)