package com.pbeagan.domain.flags

import dev.patbeagan.base.Flag
import dev.patbeagan.base.FlagSet

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