package com.pbeagan.writer

import com.pbeagan.data.Mob
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.readUTF8Line
import kotlinx.coroutines.runBlocking

class Reader(private val writer: Writer) {

    private val inputs: MutableMap<Int, ByteReadChannel> = mutableMapOf()

    fun register(m: Mob, channel: ByteReadChannel) {
        inputs[m.idForIO] = channel
    }
    fun isActive(mob: Mob): Boolean = inputs[mob.idForIO] != null

    fun read(player: Mob): String? {
        val byteReadChannel = inputs[player.idForIO]
        if (byteReadChannel == null || byteReadChannel.isClosedForRead) {
            inputs.remove(player.idForIO)
            writer.debug("Reader Removed ${player.name}")
            return null
        }

        writer.debug("Reading: ${player.name} // $byteReadChannel")

        return runBlocking { byteReadChannel.readUTF8Line() }
    }
}