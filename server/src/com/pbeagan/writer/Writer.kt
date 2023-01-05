package com.pbeagan.writer

import com.pbeagan.domain.Mob
import io.ktor.utils.io.ByteWriteChannel

interface Writer {
    fun register(m: Mob, channel: ByteWriteChannel)
    fun sayToRoomOf(m: Mob): WriteTarget
    fun sayToAll(): WriteTarget
    fun sayTo(vararg m: Mob): WriteTarget
    fun debug(s: String)
}
