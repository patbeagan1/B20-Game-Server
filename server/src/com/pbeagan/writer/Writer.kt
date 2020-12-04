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

    fun sayTo(vararg m: Mob): WriteTarget {
        require(m.isNotEmpty())
        return WriteTarget(m)
    }
    fun sayToRoomOf(m: Mob): WriteTarget = WriteTarget(m.currentRoomOtherMobs(mobs).toTypedArray())
    fun sayToAll(): WriteTarget = WriteTarget(mobs.toTypedArray())

    fun debug(s: String) = println(s)

    inner class WriteTarget(
        private val target: Array<out Mob>
    ) {

        private val horizontalRule = (0..20).joinToString("") { "-" }
        fun system(s: String, shouldIndent: Boolean = false) = write { s.formatChannel("GAME: ", shouldIndent) }
        fun info(s: String, shouldIndent: Boolean = false) = write { s.formatChannel("INFO: ", shouldIndent) }
        fun dead(s: String, shouldIndent: Boolean = false) = write { s.formatChannel("DEAD: ", shouldIndent) }
        fun combat(s: String, shouldIndent: Boolean = false) = write { s.formatChannel("CMBT: ", shouldIndent) }
        fun error(s: String, shouldIndent: Boolean = false) = write { s.formatChannel("ERR : ", shouldIndent) }
        fun move(s: String, shouldIndent: Boolean = false) = write { s.formatChannel("MOVE: ", shouldIndent) }
        fun join(s: String, shouldIndent: Boolean = false) = write { s.formatChannel("JOIN: ", shouldIndent) }

        fun horizontalRule() = write { horizontalRule }

        private fun String.formatChannel(prefix: String, indent: Boolean) =
            "\n${this}"
                .replace("\n", "\n$prefix${if (indent) "\t" else ""}")
                .trim()

        private fun write(m: WriteTarget = WriteTarget(mobs.toTypedArray()), s: (Mob) -> String) {
            debug("Output channels: " + outputs.map { t ->
                t.key to mobs.firstOrNull { it.idForIO == t.key }?.name
            }.toString())
            m.target.forEach { mob ->
                val byteWriteChannel = outputs[mob.idForIO]
                if (byteWriteChannel == null || byteWriteChannel.isClosedForWrite) {
                    outputs.remove(mob.idForIO)
                } else {

                    debug("Writing: ${mob.name} // $byteWriteChannel")

                    runBlocking { byteWriteChannel.write("${s(mob)}\n") }
                }
            }
        }

        fun turnStart(name: String) {
            write { target ->
                """
              $horizontalRule
              Turn Start: ${if (target.name == name) "$name (you)" else name}

          """.trimIndent()
            }
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

    fun read(player: Mob): String? {
//        try {
            writer.debug("Input channels: " + inputs.map { t ->
                t.key to mobs.firstOrNull { it.idForIO == t.key }?.name
            }.toString())

            val byteReadChannel = inputs[player.idForIO]
            if (byteReadChannel == null || byteReadChannel.isClosedForRead) {
                inputs.remove(player.idForIO)
                writer.debug("Removed ${player.name}")
                return null
            }

            writer.debug("Reading: ${player.name} // $byteReadChannel")

            return runBlocking { byteReadChannel.readUTF8Line() }

//        }
//        catch (e: IllegalStateException) {
//            writer.sayTo(player).error("Invalid char detected! To quit, type '^]'\n$e")
//            return null
//        }
    }
}
