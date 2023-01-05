package com.pbeagan.domain.flags

import dev.patbeagan.base.Flag
import dev.patbeagan.base.FlagSet

enum class ItemFlags : Flag {
    GLOW,
    HUM,
    INVISIBLE,
    MAGIC,
    TAKEABLE,
    UNDROPPABLE,
    LOCKED,
    KEY,
    WIELDABLE,
    CONSUMABLE,
    COLLISION,
    BLESSED;

    companion object {
        val default
            get() = FlagSet.of(
                TAKEABLE
            )
    }
}