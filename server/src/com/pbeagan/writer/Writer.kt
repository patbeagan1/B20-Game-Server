package com.pbeagan.writer

import com.pbeagan.mob.Mob
import com.pbeagan.mob.currentRoomOtherMobs
import io.ktor.util.cio.write
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.ByteWriteChannel
import io.ktor.utils.io.readUTF8Line
import kotlinx.coroutines.runBlocking
import mobs
import java.util.concurrent.atomic.AtomicInteger

class Writer {
    private val outputs: MutableMap<Int, ByteWriteChannel> = mutableMapOf()

    fun register(m: Mob, channel: ByteWriteChannel) {
        outputs[m.idForIO] = channel
    }

    fun sayTo(vararg m: Mob): WriteTarget = WriteTarget(m)
    fun sayToRoomOf(m: Mob): WriteTarget = WriteTarget(m.currentRoomOtherMobs(mobs).toTypedArray())
    fun sayToAll(): WriteTarget = WriteTarget(mobs.toTypedArray())

    fun debug(s: String) = println(s)

    inner class WriteTarget(
        private val target: Array<out Mob>
    ) {

        private val horizontalRule = (0..20).joinToString("") { "-" }
        fun room(s: String, shouldIndent: Boolean = false) = write(s.formatChannel("ROOM: ", shouldIndent))
        fun info(s: String, shouldIndent: Boolean = false) = write(s.formatChannel("INFO: ", shouldIndent))
        fun dead(s: String, shouldIndent: Boolean = false) = write(s.formatChannel("DEAD: ", shouldIndent))
        fun combat(s: String, shouldIndent: Boolean = false) = write(s.formatChannel("CMBT: ", shouldIndent))
        fun error(s: String, shouldIndent: Boolean = false) = write(s.formatChannel("ERR : ", shouldIndent))
        fun move(s: String, shouldIndent: Boolean = false) = write(s.formatChannel("MOVE: ", shouldIndent))

        fun horizontalRule() = write(horizontalRule)

        private fun String.formatChannel(n: String, indent: Boolean) =
            "\n${this.trim()}"
                .replace("\n", "\n${if (indent) "\t" else ""}$n")
                .trim()

        private fun write(s: String, m: WriteTarget = WriteTarget(mobs.toTypedArray())) {
            debug("Output channels: " + outputs.map { t ->
                t.key to mobs.firstOrNull { it.idForIO == t.key }?.name
            }.toString())
            m.target.forEach { mob ->
                runBlocking { outputs[mob.idForIO]?.write("$s\n") }
            }
        }

        fun turnStart(name: String) {
            write(
                """
              $horizontalRule
              Turn Start: $name
              $horizontalRule
          """.trimIndent()
            )
        }
    }
}

object IDforIOGenerator {
    private val current = AtomicInteger(0)
    fun get() = current.incrementAndGet()
}

class Reader(private val writer: Writer) {

    private val inputs: MutableMap<Int, ByteReadChannel> = mutableMapOf()

    fun register(m: Mob, channel: ByteReadChannel) {
        inputs[m.idForIO] = channel
    }

    fun read(player: Mob): String? = try {
        writer.debug("Input channels: " + inputs.map { t ->
            t.key to mobs.firstOrNull { it.idForIO == t.key }?.name
        }.toString())
        val byteReadChannel = inputs[player.idForIO]
        writer.debug(byteReadChannel.toString())
        writer.debug("Reading: ${player.name} // $byteReadChannel")
        if (byteReadChannel?.isClosedForRead == true) {
            null
        } else {
            runBlocking { byteReadChannel?.readUTF8Line() }
        }
    } catch (e: IllegalStateException) {
        writer.sayToAll().error("Invalid char detected! To quit, type '^]'")
        null
    }
}
