package com.pbeagan.contextual.actions.type

import com.pbeagan.contextual.Mob
import com.pbeagan.data.writer.Writer

abstract class Action {
    abstract operator fun invoke(self: Mob)
    lateinit var writer: Writer
}

