import com.pbeagan.models.*
import mob.Mob

object SampleData {
    val mobs = mutableListOf(
        Mob("Alice", baseAtkMelee = 1, armor = 3, hearts = 25, behavior = MobBehavior.PLAYER),
        Mob(
            "Bob",
            baseAtkMelee = 2,
            armor = 2,
            hearts = 14,
            behavior = MobBehavior.AGGRESSIVE,
            location = 2
        )
    )
    val rooms = listOf(
        RoomData(
            0,
            name = "The Front Porch",
            descriptionLook = "The front porch of Liz's house",
            directions = listOf(RoomDirectionData(Direction.EAST, 1, "The Road")),
            roomFlags = flagSet(RoomFlags.INDOORS),
            items = mutableListOf(
                ItemData(
                    0,
                    listOf("Rusty Sword", "sword"),
                    "It's got a few notches in it.",
                    "A rusty sword is lying on the ground."
                )
            )
        ),
        RoomData(
            1,
            name = "The Road",
            descriptionLook = "A portal to new places",
            directions = listOf(
                RoomDirectionData(Direction.WEST, 0, "The safest place around"),
                RoomDirectionData(Direction.NORTH, 2, "In the distance you see an ice field.")
            )
        ),
        RoomData(
            2,
            name = "A bridge across the fjord",
            descriptionLook = "A sturdy metal bridge spans the gap between two fissues in an icy field.",
            directions = listOf(RoomDirectionData(Direction.SOUTH, 1, "There is a road to the south."))
        )
    ).associateBy { it.id }
}