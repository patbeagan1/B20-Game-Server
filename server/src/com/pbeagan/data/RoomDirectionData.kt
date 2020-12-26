package com.pbeagan.data

import com.pbeagan.util.FlagCombined
import com.pbeagan.util.createFlagSet

data class RoomDirectionData(
    val destinationID: Int,
    val exitConditions: FlagCombined<ExitCondition> = createFlagSet()
)