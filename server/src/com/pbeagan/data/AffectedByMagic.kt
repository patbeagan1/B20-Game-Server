package com.pbeagan.data

import com.pbeagan.models.Flag
import com.pbeagan.models.createFlagSet

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