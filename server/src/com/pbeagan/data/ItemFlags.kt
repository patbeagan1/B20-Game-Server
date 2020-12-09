package com.pbeagan.data

import com.pbeagan.models.Flag
import com.pbeagan.models.createFlagSet

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
    BLESSED;

    companion object {
        val default
            get() = createFlagSet(
                TAKEABLE
            )
    }
}