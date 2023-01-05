package com.pbeagan.domain

import com.pbeagan.domain.flags.ExitCondition
import com.pbeagan.util.FlagSet

data class RoomDirectionData(
    val destinationID: Int,
    val exitConditions: FlagSet<ExitCondition> = FlagSet.of(),
)