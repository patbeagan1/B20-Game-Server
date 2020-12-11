package com.pbeagan.data

import com.pbeagan.util.FlagCombined
import com.pbeagan.util.createFlagSet

data class RoomDirectionData(
    val direction: Direction,
    val destinationID: Int,
    val preview: String,
    val exitConditions: FlagCombined<ExitCondition> = createFlagSet()
) {
    companion object {
        fun from(s: String): RoomDirectionData {
            val directionChar = s[0]
            val destinationIDChar = s[2]
            val conditions = s.substringAfter("flags=").substringBefore(" ")
            val preview = s.substringAfter("preview=")
            return RoomDirectionData(
                Direction.values().first { it.name.startsWith(directionChar) },
                destinationIDChar.toInt(),
                preview,
                FlagCombined(conditions.toInt())
            )
        }
    }
}