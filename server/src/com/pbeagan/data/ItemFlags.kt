package com.pbeagan.data

import com.pbeagan.util.Flag
import com.pbeagan.util.FlagSet

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