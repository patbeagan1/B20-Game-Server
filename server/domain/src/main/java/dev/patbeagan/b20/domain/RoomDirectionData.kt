package dev.patbeagan.b20.domain

import dev.patbeagan.b20.domain.flags.ExitCondition
import dev.patbeagan.base.FlagSet

data class RoomDirectionData(
    val destinationID: Int,
    val exitConditions: FlagSet<ExitCondition> = FlagSet.of(),
)