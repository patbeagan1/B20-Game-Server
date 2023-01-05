package com.pbeagan.domain

import com.pbeagan.domain.flags.ExitCondition
import dev.patbeagan.base.FlagSet

data class RoomDirectionData(
    val destinationID: Int,
    val exitConditions: FlagSet<ExitCondition> = FlagSet.of(),
)