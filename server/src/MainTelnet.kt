import dev.patbeagan.base.ApplyOnce
import com.pbeagan.Game
import com.pbeagan.WorldState
import com.pbeagan.demo.SampleData
import com.pbeagan.data.reader.Reader
import com.pbeagan.data.writer.Writer
import com.pbeagan.data.writer.WriterImpl
import com.pbeagan.signIn
import io.ktor.network.selector.ActorSelectorManager
import io.ktor.network.sockets.aSocket
import io.ktor.network.sockets.openReadChannel
import io.ktor.network.sockets.openWriteChannel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main(args: Array<String>) {
    runBlocking {
        val server = aSocket(ActorSelectorManager(Dispatchers.IO))
            .tcp()
            .bind("127.0.0.1", 2324)
        println("Started echo telnet server at ${server.localAddress}")

        val worldState = WorldState(
            SampleData.sampleMobs,
            SampleData.sampleRooms
        )
        val writer: Writer = WriterImpl(worldState)
        val reader = Reader(writer)
        val game = Game(worldState)
        val startGame = ApplyOnce {
            launch { startGameLoop(game, writer, reader) }
        }

        while (true) {
            val socket = server.accept()

            launch {
                println("Socket accepted: ${socket.remoteAddress}")

                val writeChannel = socket.openWriteChannel(autoFlush = true)
                val readChannel = socket.openReadChannel()

                val player = signIn(worldState, readChannel, writeChannel)

                writer.register(player, writeChannel)
                reader.register(player, readChannel)

                writer.sayTo(player).run {
                    horizontalRule()
                    system("Welcome, ${player.nameStyled}!")
                    horizontalRule()
                }

                startGame(Unit)
            }
        }
    }
}

private fun startGameLoop(
    game: Game,
    writer: Writer,
    reader: Reader,
) {
    var turnCount = 0
    while (true) {
        writer.sayToAll().run {
            horizontalRule()
            system("Turn ${turnCount++}")
        }
        game.gameLoop(writer, reader)
    }
}
