package com.pbeagan

import com.pbeagan.data.Mob
import io.ktor.util.cio.write
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.ByteWriteChannel
import io.ktor.utils.io.readUTF8Line
import mobs

class Account {
    suspend fun signIn(
        input: ByteReadChannel,
        writer: ByteWriteChannel
    ): Mob {
        while (true) {
            writer.write(
                """Welcome to B20MUD! 
               |What is your name?
               |""".trimMargin()
            )
            val line = input.readUTF8Line()

            mobs.firstOrNull {
                it.isPlayer && it.nameBase.toLowerCase() == line?.toLowerCase()
            }?.also {
                return it
            } ?: writer.write("Sorry, no players like that here...")
        }
    }
}