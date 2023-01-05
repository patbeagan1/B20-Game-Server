package com.pbeagan.domain.flags

import com.pbeagan.util.Flag
import com.pbeagan.util.FlagSet

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
            get() = FlagSet.of(
                INVISIBLE
            )
    }
}