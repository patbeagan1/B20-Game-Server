package com.pbeagan.data.writer

import com.pbeagan.domain.Mob

interface WriterInternal {
    fun write(mob: Mob, stringGenerator: (Mob) -> String)
}