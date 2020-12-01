import com.pbeagan.models.room.Entrance
import com.pbeagan.models.room.Exit
import com.pbeagan.models.room.Room
import com.pbeagan.models.room.RoomX
import com.pbeagan.models.worldstate.*
import com.pbeagan.models.worldstate.Mob
import site.controllers.CounterService
import site.controllers.CounterServiceImpl
import org.koin.dsl.module

import loggerGen

object Injection {

    val helloAppModule = module {
        single<CounterService> { CounterServiceImpl() }
        single {
            WorldState(
                WorldStateX(
                    mapOf(
                        "The Front Porch" to Room(
                            RoomX(
                                "There is still a long way to go.",
                                Entrance("west"),
                                listOf(Exit("east", "The Wide World")),
                                listOf(),
                                listOf()
                            )
                        )
                    ),
                    listOf(
                        getSamplePlayer(), getSamplePlayer()
                    )
                )
            )
        }
        factory { loggerGen(this::class.java) }
    }

    private fun getSamplePlayer(): Mob {
        return Mob(
            "Cloud",
            3,
            Attr(
                3,
                4,
                4,
                10,
                10,
                10,
                10,
                10,
                10,
                10,
                10,
                10,
                10
            ),
            3,
            4,
            5,
            6,
            3,
            4,
            Location(x = 1.0, y = 2.0),
            Items(ItemsX(2.0)),
            Items(ItemsX(2.0))
        )
    }
}