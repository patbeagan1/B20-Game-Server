package com.pbeagan.data

import com.pbeagan.util.FlagSet

data class RoomDirectionData(
    val destinationID: Int,
    val exitConditions: FlagSet<ExitCondition> = FlagSet.of(),
)