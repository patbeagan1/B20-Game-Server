package com.pbeagan.data.writer

import dev.patbeagan.b20.contextual.Mob

interface WriterInternal {
    fun write(mob: Mob, stringGenerator: (Mob) -> String)
}