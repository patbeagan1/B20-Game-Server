package com.pbeagan.data

import com.pbeagan.util.Flag
import com.pbeagan.util.FlagCombined
import com.pbeagan.util.createFlagSet

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