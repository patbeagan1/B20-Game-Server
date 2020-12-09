package com.pbeagan.writer

import com.pbeagan.data.Mob
import com.pbeagan.data.currentRoomOtherMobsAndSelf
import io.ktor.util.cio.write
import io.ktor.utils.io.ByteWriteChannel
import kotlinx.coroutines.runBlocking
import mobs
import java.io.IOException
import java.util.concurrent.ConcurrentHashMap

class WriterImpl : Writer, WriterInternal {
    private val outputs: ConcurrentHashMap<Int, ByteWriteChannel> = ConcurrentHashMap()

    override fun register(m: Mob, channel: ByteWriteChannel) {
        outputs[m.idForIO] = channel
    }

    override fun sayToRoomOf(m: Mob): WriteTarget =
        WriteTarget(this, m.currentRoomOtherMobsAndSelf(mobs).toTypedArray())

    override fun sayToAll(): WriteTarget =
        WriteTarget(this, mobs.toTypedArray())

    override fun sayTo(vararg m: Mob): WriteTarget {
        require(m.isNotEmpty())
        return WriteTarget(this, m)
    }

    override fun debug(s: String) = println(s)

    override fun write(mob: Mob, s: (Mob) -> String) {
        val byteWriteChannel = outputs[mob.idForIO]
        if (byteWriteChannel == null || byteWriteChannel.isClosedForWrite) {
            outputs.remove(mob.idForIO)
        } else {
            debug("Writing: ${mob.name} // $byteWriteChannel")
            try {
                runBlocking { byteWriteChannel.write("${s(mob)}\n") }
            } catch (e: IOException) {
                debug(e.toString())
                outputs.remove(mob.idForIO)
            }
        }
    }
}

interface WriterInternal {
    fun write(s1: Mob, s: (Mob) -> String)
}

interface Writer {
    fun register(m: Mob, channel: ByteWriteChannel)
    fun sayToRoomOf(m: Mob): WriteTarget
    fun sayToAll(): WriteTarget
    fun sayTo(vararg m: Mob): WriteTarget
    fun debug(s: String)
}
