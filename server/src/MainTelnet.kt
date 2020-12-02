import actions.Action
import actions.AttackMelee
import actions.Doors
import actions.Examine
import actions.Inventory
import actions.Look
import actions.Move
import actions.Pass
import actions.Retry
import actions.Take
import com.pbeagan.models.Direction.DOWN
import com.pbeagan.models.Direction.EAST
import com.pbeagan.models.Direction.NORTH
import com.pbeagan.models.Direction.SOUTH
import com.pbeagan.models.Direction.UP
import com.pbeagan.models.Direction.WEST
import com.pbeagan.models.MobBehavior.AGGRESSIVE
import com.pbeagan.models.MobBehavior.FEARFUL
import com.pbeagan.models.MobBehavior.HELPFUL
import com.pbeagan.models.MobBehavior.IMMOBILE
import com.pbeagan.models.MobBehavior.LOOTER
import com.pbeagan.models.MobBehavior.PLAYER
import com.pbeagan.models.MobBehavior.WANDERER
import io.ktor.network.selector.ActorSelectorManager
import io.ktor.network.sockets.aSocket
import io.ktor.network.sockets.openReadChannel
import io.ktor.network.sockets.openWriteChannel
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import mob.Mob
import mob.attackFirstVisible
import writer.Reader
import writer.Writer
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

        while (true) {
            val socket = server.accept()

            launch {
                println("Socket accepted: ${socket.remoteAddress}")

                val input = socket.openReadChannel()
                val output = socket.openWriteChannel(autoFlush = true)
                val writer = Writer(output)
                val reader = Reader(input, writer)
                try {
                    val player = signIn(reader, writer)
                    writer.horirule()
                    writer.room("Welcome, ${player.name}!")
                    writer.horirule()
                    while (true) {
                        gameLoop(writer, reader)
                    }
                } catch (e: Throwable) {
                    e.printStackTrace()
                    socket.close()
                }
            }
        }
    }
}

private suspend fun signIn(
    input: Reader,
    writer: Writer
): Mob {
    while (true) {
        writer.info(
            """Welcome to B20MUD! 
               |What is your name?
               |""".trimMargin()
        )
        val line = input.read()

        mobs.firstOrNull {
            it.behavior == PLAYER && it.name.toLowerCase() == line?.toLowerCase()
        }?.also {
            return it
        } ?: writer.error("Sorry, no players like that here...")
    }
}

private suspend fun gameLoop(writer: Writer, reader: Reader) {
    mobs.apply { sortByDescending { roll20() + it.attr.awareness } }.forEach { mob ->
        var action: Action? = null
        while (action == null || action is Retry) {
            action = mob.getAction(reader)
            if (action is Retry) {
                writer.error(action.errorMsg)
            }
        }
        mob.action = action
    }
    mobs.forEach { it.action.apply { this.writer = writer }(it) }
}

private suspend fun Mob.getAction(reader: Reader): Action? = when (behavior) {
    PLAYER -> interpretPlayerAction(reader, this)
    HELPFUL -> TODO()
    IMMOBILE -> TODO()
    LOOTER -> TODO()
    AGGRESSIVE -> {
        when (roll20()) {
            in 0..18 -> attackFirstVisible(mobs)
            else -> Pass
        }
    }
    WANDERER -> TODO()
    FEARFUL -> TODO()
}

private suspend fun interpretPlayerAction(reader: Reader, mob: Mob): Action? {
    val input = reader.read()?.toLowerCase() ?: return Retry("Unknown Command")
    val arg = " *([^\\s]*)?"
    return listOf<Pair<String, ((List<String>) -> Action)>>(
        // Util
        "(\\.|\\n|again)" to { i -> mob.action },
        "debug$arg" to { i ->
            Pass.also { pass ->
                pass.writer.info(
                    mobs.firstOrNull {
                        it.name.toLowerCase() == i.getOrNull(1)?.toLowerCase()
                    }.toString()
                )
            }
        },

        // INFO
        "l(s|ook)?" to { i -> Look() },
        "do(or(s)?)?" to { i -> Doors() },
        "exit(s)?" to { i -> Doors() },
        "ex(amine)?$arg" to { i -> Examine.getOrRetry(i) },

        // Items
        "i(nventory)?" to { i -> Inventory() },
        "take$arg" to { i -> Take.getOrRetry(i, mob) },

        // Movement
        "n(orth)?" to { i -> Move(NORTH) },
        "s(outh)?" to { i -> Move(SOUTH) },
        "e(ast)?" to { i -> Move(EAST) },
        "w(est)?" to { i -> Move(WEST) },
        "u(p)?" to { i -> Move(UP) },
        "d(own)?" to { i -> Move(DOWN) },

        // Combat
        "(atk|attack)$arg" to { i -> AttackMelee.attackOrRetry(i, mob) }
    ).map { it.first.toRegex() to it.second }
        .firstOrNull { it.first.matches(input) }
        ?.let { pair ->
            pair.first.find(input)
                ?.groupValues
                ?.also { println("Matches: $it") }
                ?.let { pair.second.invoke(it) }
        } ?: Retry("Unknown Command")
}
