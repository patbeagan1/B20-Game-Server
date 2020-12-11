package com.pbeagan.data

import com.pbeagan.util.Flag
import com.pbeagan.util.createFlagSet

enum class AffectedByMagic : Flag {
    INVISIBLE,
    SLEEP,
    CURSE,
    FIRE,
    ELECTRIC,
    FROST,
    PARALYSIS;

    companion object {
        val defaultItem
            get() = createFlagSet(
                INVISIBLE
            )
    }
}