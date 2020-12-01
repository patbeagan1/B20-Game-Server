import actions.*
import com.pbeagan.models.Direction.*
import com.pbeagan.models.MobBehavior.*
import io.ktor.network.selector.ActorSelectorManager
import io.ktor.network.sockets.aSocket
import io.ktor.network.sockets.openReadChannel
import io.ktor.network.sockets.openWriteChannel
import io.ktor.util.KtorExperimentalAPI
import io.ktor.util.cio.write
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.readUTF8Line
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import mob.Mob
import mob.attackFirstVisible
import mob.currentRoom
import mob.currentRoomOtherMobs
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
                val reader = Reader(input)
                try {
                    val player = signIn(input, writer)
                    writer.horirule()
                    writer.room("Welcome, ${player.name}!")
                    writer.horirule()

                    while (true) {
                        gameLoop(writer, reader)
                    }
                } catch (e: IllegalStateException) {
                    output.write("Invalid char detected! To quit, type '^]'")
                } catch (e: Throwable) {
                    e.printStackTrace()
                    socket.close()
                }
            }
        }
    }
}

private suspend fun signIn(
    input: ByteReadChannel,
    writer: Writer
): Mob {
    while (true) {
        writer.info(
            """Welcome to B20MUD! 
               |What is your name?
               |""".trimMargin()
        )
        val line = input.readUTF8Line()

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
            if(action is Retry){
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
    val input = reader.input.readUTF8Line()?.toLowerCase() ?: return Retry("Unknown Command")
    val arg = " *([^\\s]*)?"
    val list = listOf<Pair<String, ((List<String>) -> Action)>>(
        // Util
        "(\\.|\\n|again)" to { i -> mob.action },
        "debug$arg" to { i ->
            Pass.also { pass ->
                pass.writer.info(mobs.firstOrNull {
                    it.name.toLowerCase() == i.getOrNull(1)?.toLowerCase()
                }.toString())
            }
        },

        // INFO
        "l(s|ook)?" to { i -> Look() },
        "do(or(s)?)?" to { i -> Doors() },
        "exit(s)?" to { i -> Doors() },
        "ex(amine)?$arg" to { i -> examineOrRetry(i) },

        // Items
        "i(nventory)?" to { i -> Inventory() },
        "take$arg" to { i -> takeOrRetry(i, mob) },

        // Movement
        "n(orth)?" to { i -> Move(NORTH) },
        "s(outh)?" to { i -> Move(SOUTH) },
        "e(ast)?" to { i -> Move(EAST) },
        "w(est)?" to { i -> Move(WEST) },
        "u(p)?" to { i -> Move(UP) },
        "d(own)?" to { i -> Move(DOWN) },

        // Combat
        "(atk|attack)$arg" to { i -> attackOrRetry(i, mob) }
    )
    return list.map { it.first.toRegex() to it.second }
        .firstOrNull { it.first.matches(input) }
        ?.let { pair ->
            pair.first.find(input)
                ?.groupValues
                ?.also { println("Matches: $it") }
                ?.let { pair.second.invoke(it) }
        } ?: Retry("Unknown Command")
}

private fun examineOrRetry(i: List<String>): Action = i
    .getOrNull(2)
    ?.let { Examine(it) }
    ?: Retry("What should I examine?")

private fun takeOrRetry(i: List<String>, mob: Mob): Action {
    val item = i.getOrNull(1)
    val firstOrNull = mob.currentRoom()
        ?.items
        ?.firstOrNull { itemData ->
            itemData.names.any { name ->
                item?.let { name.earlyMatches(it) } ?: false
            }
        }

    return safeLet(item, firstOrNull) { _, safeItem ->
        Take(safeItem)
    } ?: run {
        if (item == null) Retry("What would you like to take?") else null
    } ?: run {
        if (firstOrNull == null) Retry("That item isn't here...") else null
    } ?: Pass
}

private fun attackOrRetry(i: List<String>, mob: Mob): Action {
    val target = i[2]
    return when {
        target.isEmpty() -> mob.attackFirstVisible(mobs)
        else -> mob.currentRoomOtherMobs(mobs)
            .firstOrNull { it.name.toLowerCase() == target }
            ?.let { AttackMelee(it) }
    } ?: Retry("Looks like that mob isn't here...")
}