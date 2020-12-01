package writer

import io.ktor.util.cio.write
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.ByteWriteChannel
import kotlinx.coroutines.runBlocking

class Writer(private val output: ByteWriteChannel) {
    fun room(s: String) = write(s.format("ROOM: "))
    fun horirule() = write((0..20).joinToString("") { "-" })
    fun info(s: String) = write(s.format("INFO: "))
    fun dead(s: String) = write(s.format("DEAD: "))
    fun combat(s: String) = write(s.format("CMBT: "))
    fun error(s: String) = write(s.format("ERR : "))
    private fun String.format(n: String) =
        "\n${this.trim()}"
            .replace("\n", "\n$n")
            .trim()

    private fun write(s: String) = runBlocking { output.write("$s\n") }
}

class Reader(val input: ByteReadChannel)