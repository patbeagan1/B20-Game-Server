package com.pbeagan.data.writer

import com.pbeagan.contextual.Mob
import com.pbeagan.contextual.currentRoomOtherMobsAndSelf
import io.ktor.utils.io.ByteWriteChannel
import io.ktor.utils.io.writeFully
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
        WriteTarget(this, m.currentRoomOtherMobsAndSelf(mobs))

    override fun sayToAll(): WriteTarget =
        WriteTarget(this, mobs)

    override fun sayTo(vararg m: Mob): WriteTarget {
        require(m.isNotEmpty())
        return WriteTarget(this, m.map { it })
    }

    override fun debug(s: String) = println(s)

    override fun write(mob: Mob, stringGenerator: (Mob) -> String) {
        val byteWriteChannel = outputs[mob.idForIO]
        if (byteWriteChannel == null || byteWriteChannel.isClosedForWrite) {
            outputs.remove(mob.idForIO)
        } else {
            debug("Writing: ${mob.nameStyled} // $byteWriteChannel")
            try {
                runBlocking { byteWriteChannel.writeFully("${stringGenerator(mob)}\n".toByteArray()) }
            } catch (e: IOException) {
                debug(e.toString())
                outputs.remove(mob.idForIO)
            }
        }
    }
}