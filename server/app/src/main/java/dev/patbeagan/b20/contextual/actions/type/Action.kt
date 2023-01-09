package com.pbeagan.contextual.actions.type

import dev.patbeagan.b20.contextual.Mob
import com.pbeagan.data.writer.Writer

abstract class Action {
    abstract operator fun invoke(self: Mob)
    lateinit var writer: Writer
}

