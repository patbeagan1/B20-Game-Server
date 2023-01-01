package com.pbeagan

import com.pbeagan.data.Mob
import io.ktor.util.cio.write
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.ByteWriteChannel
import io.ktor.utils.io.readUTF8Line
import io.ktor.utils.io.writeFully
import mobs

class Account {
    suspend fun signIn(
        input: ByteReadChannel,
        writer: ByteWriteChannel
    ): Mob {
        while (true) {
            writer.writeFully(
                """Welcome to B20MUD! 
                       |What is your name?
                       |""".trimMargin()
                    .toByteArray()
            )
            val line = input.readUTF8Line()

            mobs.firstOrNull {
                it.isPlayer && it.nameBase.equals(line, ignoreCase = true)
            }?.also {
                return it
            } ?: writer.writeFully("Sorry, no players like that here...".toByteArray())
        }
    }
}