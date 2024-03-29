package dev.patbeagan.b20.domain.flags

import dev.patbeagan.base.Flag
import dev.patbeagan.base.FlagSet

enum class VisibleBy : Flag {
    SIGHT,
    SOUND,
    SENSE_LIFE,
    DETECT_EVIL,
    DETECT_INVISIBLE,
    DETECT_MAGIC;

    companion object {
        val defaultMob: FlagSet<VisibleBy>
            get() = FlagSet.of(
                SIGHT,
                SOUND,
                SENSE_LIFE
            )
        val defaultItem: FlagSet<VisibleBy>
            get() = FlagSet.of(
                SIGHT
            )
    }
}

enum class Lighting {
    BRIGHT, DIM, DARK, NONE
}