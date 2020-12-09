package com.pbeagan.data

import com.pbeagan.models.Flag
import com.pbeagan.models.FlagCombined
import com.pbeagan.models.createFlagSet

enum class VisibleBy : Flag {
    SIGHT,
    SOUND,
    SENSE_LIFE,
    DETECT_EVIL,
    DETECT_INVISIBLE,
    DETECT_MAGIC;

    companion object {
        val defaultMob: FlagCombined<VisibleBy>
            get() = createFlagSet(
                SIGHT,
                SOUND,
                SENSE_LIFE
            )
        val defaultItem: FlagCombined<VisibleBy>
            get() = createFlagSet(SIGHT)
    }
}