package com.pbeagan.data.writer

import com.pbeagan.contextual.Mob

interface WriterInternal {
    fun write(mob: Mob, stringGenerator: (Mob) -> String)
}