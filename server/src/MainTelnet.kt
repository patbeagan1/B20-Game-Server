import com.pbeagan.Account
import com.pbeagan.Game
import com.pbeagan.SampleData
import com.pbeagan.writer.Reader
import com.pbeagan.writer.Writer
import io.ktor.network.selector.ActorSelectorManager
import io.ktor.network.sockets.aSocket
import io.ktor.network.sockets.openReadChannel
import io.ktor.network.sockets.openWriteChannel
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.net.InetSocketAddress

val rooms = SampleData.rooms
val mobs = SampleData.mobs

@KtorExperimentalAPI
fun main(args: Array<String>) {
    runBlocking {
        val server = aSocket(ActorSelectorManager(Dispatchers.IO))
            .tcp()
            .bind(InetSocketAddress("127.0.0.1", 2323))
        println("Started echo telnet server at ${server.localAddress}")

        val writer = Writer()
        val reader = Reader(writer)
        val game = Game()
        val startGame = ApplyOnce(Unit) {
            launch { startGameLoop(game, writer, reader) }
        }

        while (true) {
            val socket = server.accept()

            launch {
                println("Socket accepted: ${socket.remoteAddress}")

                try {
                    val account = Account()
                    val writeChannel = socket.openWriteChannel(autoFlush = true)
                    val readChannel = socket.openReadChannel()
                    val player = account.signIn(readChannel, writeChannel)
                    writer.register(player, writeChannel)
                    reader.register(player, readChannel)

                    startGame()

                    writer.sayTo().run {
                        horizontalRule()
                        room("Welcome, ${player.name}!")
                        horizontalRule()
                    }

                } catch (e: Throwable) {
                    e.printStackTrace()
                    socket.close()
                }
            }
        }
    }
}

class ApplyOnce<T, R>(private val t: T, private val action: T.() -> R) {
    var hasInvoked = false
    operator fun invoke(): R {
        hasInvoked = true
        return t.action()
    }
}

private suspend fun startGameLoop(
    game: Game,
    writer: Writer,
    reader: Reader
) {
    var turnCount = 0
    while (true) {
        game.gameLoop(writer, reader)
        writer.sayToAll().run {
            horizontalRule()
            room("Turn ${turnCount++}")
        }
    }
}
