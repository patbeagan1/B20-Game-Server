package com.pbeagan.data.reader

import com.pbeagan.domain.Mob
import com.pbeagan.data.writer.Writer
import io.ktor.utils.io.*
import kotlinx.coroutines.runBlocking

class Reader(private val writer: Writer) {

    private val inputs: MutableMap<Int, ByteReadChannel> = mutableMapOf()

    fun register(m: Mob, channel: ByteReadChannel) {
        inputs[m.idForIO] = channel
    }

    fun isActive(mob: Mob): Boolean = inputs[mob.idForIO] != null

    fun read(player: Mob): String? {
        // todo this spins in practice if everyone disconnects
        //  putting this in to slow it down - should find a better solution
        if (inputs.isEmpty()) {
            Thread.sleep(5000)
        }

        val byteReadChannel = inputs[player.idForIO]
        if (byteReadChannel == null || byteReadChannel.isClosedForRead) {
            remove(player)
            return null
        }

        writer.debug("Reading: ${player.nameStyled} // $byteReadChannel")

        return runBlocking {
            try {
                byteReadChannel.readUTF8Line()
            } catch (e: IllegalStateException) {
                writer.debug(e.toString())
                writer.sayTo(player)
                    .error("Something went wrong! We can't read your input anymore. Please log back in.")
                writer.sayTo(player)
                    .error("If you are trying to close the client, type ^]. Then, ^C will work.")
                remove(player)
                null
            }
        }
    }

    private fun remove(player: Mob) {
        inputs.remove(player.idForIO)
        writer.debug("Reader Removed ${player.nameStyled}")
    }
}