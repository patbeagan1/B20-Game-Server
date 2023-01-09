package dev.patbeagan.b20.demo

import com.pbeagan.contextual.*
import dev.patbeagan.b20.contextual.Mob.*
import com.pbeagan.contextual.ancestry.Goblin
import com.pbeagan.contextual.ancestry.Hobgoblin
import com.pbeagan.contextual.ancestry.Human
import dev.patbeagan.b20.contextual.ItemData
import dev.patbeagan.b20.contextual.Mob
import dev.patbeagan.b20.contextual.RoomData
import dev.patbeagan.b20.domain.Exits
import dev.patbeagan.b20.domain.RoomDirectionData
import dev.patbeagan.b20.domain.flags.ItemFlags
import dev.patbeagan.b20.domain.flags.Lighting
import dev.patbeagan.b20.domain.flags.RoomFlags
import dev.patbeagan.b20.domain.stats.DefenseValue
import dev.patbeagan.b20.domain.types.Direction.DOWN
import dev.patbeagan.b20.domain.types.Direction.EAST
import dev.patbeagan.b20.domain.types.Direction.NORTH
import dev.patbeagan.b20.domain.types.Direction.SOUTH
import dev.patbeagan.b20.domain.types.Direction.UP
import dev.patbeagan.b20.domain.types.Direction.WEST
import dev.patbeagan.base.FlagSet
import dev.patbeagan.consolevision.types.coord

object SampleData {
    val sampleMobs = listOf(
        Mob(
            "Alice",
//            armor = 3,
            effects = listOf(Human()),
            isPlayer = true,
            description = object : Description {},
            locationInRoom = 2 coord 3
        ),
        Mob(
            "Steve",
            armor = DefenseValue(3),
            isPlayer = true,
            effects = listOf(Human()),
            description = object : Description {},
            locationInRoom = 2 coord 4
        ),
        Mob(
            "Bob",
            armor = DefenseValue(2),
            effects = listOf(Goblin()),
            behavior = MobBehavior.AGGRESSIVE,
            room = 2,
            description = object : Description {},
            locationInRoom = 5 coord 5
        ),
        Mob(
            "Charlie",
            armor = DefenseValue(2),
            effects = listOf(Hobgoblin()),
            behavior = MobBehavior.LOOTER,
            room = 3,
            description = object : Description {},
            locationInRoom = 1 coord 1
        ),
        Mob(
            "Darla",
            armor = DefenseValue(2),
            effects = listOf(Goblin()),
            behavior = MobBehavior.WANDERER,
            room = 0,
            description = object : Description {},
            locationInRoom = 5 coord 5
        )
    )
    val sampleRooms = listOf(
        RoomData(
            0,
            name = "The Front Porch",
            descriptionLook = "The front porch of Liz's house",
            descriptionPreview = {
                when (it) {
                    NORTH, SOUTH, UP, DOWN -> TODO()
                    EAST -> "You see a door leading outside."
                    WEST -> "You see a safe area ahead"
                }
            },
            exits = Exits(
                east = RoomDirectionData(1),
                west = RoomDirectionData(3)
            ),
            terrainString = """
                ''''''''
                ****''''
                ---*''''
                --------
                ---*''''
                ****''''
                ''''''''
            """.trimIndent(),
            roomFlags = FlagSet.of(RoomFlags.INDOORS),
            items = mutableListOf(
                ItemData(
                    0,
                    listOf("Rusty Sword", "sword"),
                    "It's got a few notches in it.",
                    "A rusty sword is lying on the ground.",
                    6 coord 6
                ).apply {
                    itemFlags.add(
                        ItemFlags.TAKEABLE,
                        ItemFlags.WIELDABLE
                    )
                },
                ItemData(
                    1,
                    listOf(
                        "Spoiled Red Apple", "fruit", "apple", "food"
                    ),
                    descriptionOnExamination = "A spoiled red apple. It does not look good enough to eat!",
                    descriptionInRoom = "There is an apple laying on the ground",
                    locationInRoom = 4 coord 4
                ).apply {
                    setItemFlags(
                        ItemFlags.CONSUMABLE,
                        object : ItemData.FlagHandler {
                            override fun invoke(self: Mob) {
                                self.hearts -= 1
                            }
                        })
                }
            )
        ),
        RoomData(
            1,
            name = "The Road",
            descriptionLook = "A portal to new places",
            descriptionPreview = {
                when (it) {
                    EAST, SOUTH -> "You see a road in this direction"
                    WEST -> TODO()
                    UP -> TODO()
                    NORTH -> TODO()
                    DOWN -> TODO()
                }
            },
            exits = Exits(
                west = RoomDirectionData(0),
                north = RoomDirectionData(2)
            ),
            terrainString = """
            ''''-'''
            ''''-'''
            '''--'''
            ''--''''
            ---'''''
            ''''''''
            ''''''''
        """.trimIndent()
        ),
        RoomData(
            2,
            name = "A bridge across the fjord",
            descriptionLook = "A sturdy metal bridge spans the gap between two fissures in an icy field.",
            descriptionPreview = { "There is a bridge in this direction." },
            exits = Exits(
                south = RoomDirectionData(1),
                north = RoomDirectionData(4)
            ),
            terrainString = """
                ''''''--''''''
                '''''*--*'''''
                '*'*'*--*'*'*'
                ~~~~~*--*~~~~~
                ~~~~~*--*~~~~~
                '*'*'*--*'*'*'
                '''''*--*'''''
                ''''''--''''''
            """.trimIndent()
        ),
        RoomData(
            4,
            name = "A vast plain",
            descriptionLook = "A large, open area lies before you. There is nothing here but earth, sky and wind.",
            descriptionPreview = { "A gust of wind comes in from this direction" },
            exits = Exits(
                south = RoomDirectionData(2)
            ),
            lighting = Lighting.DARK,
            terrainString = """
                ''''''''''''''''''''''''''''''''''''''''''''''
                '''''''''''''''--'''''''''''''-'''''''''''''''
                ''''''''''''''''''''''''''''''''''''''''''''''
                ''''''''''''''''''''''''''''''''''''''''''''''
                '''''''''''''''--'''''''''''''-'''''''''''''''
                ''''''''''''''*'-*'''''''''''-'*''''''''''''''
                ''''''''''''''''''''''''''''''''''''''''''''''
                '''''''''''''''--'''''''''''''-'''''''''''''''
                ''''''''''''''''''''''''''''''''''''''''''''''
                '''''''''''''''''''--''''''--'''''''''''''''''
                ''''''''''''''''''''''''''''''''''''''''''''''
                ''''''''''''''''''''''''''''''''''''''''''''''
                ''''''''''''''''''''''''''''''''''''''''''''''
                ''''''''''''''''''''''''''''''''''''''''''''''
                ''''''''''''''''''''''''''''''''''''''''''''''
            """.trimIndent()
        ), RoomData(
            3,
            name = "The Kitchen",
            descriptionLook = "A metal table is laden with various fruits and vegetables.",
            descriptionPreview = {
                when (it) {
                    NORTH -> TODO()
                    EAST -> TODO()
                    SOUTH -> TODO()
                    WEST -> "There is a door leading inside"
                    UP -> TODO()
                    DOWN -> TODO()
                }
            },
            exits = Exits(
                east = RoomDirectionData(0)
            ),
            items = mutableListOf(
                ItemData(
                    1,
                    listOf(
                        "Red Apple", "fruit", "apple", "food"
                    ),
                    descriptionOnExamination = "A polished red apple. It looks good enough to eat!",
                    descriptionInRoom = "There is an apple in a basket on the table",
                    locationInRoom = 1 coord 1
                ).apply
                {
                    setItemFlags(
                        ItemFlags.CONSUMABLE,
                        object : ItemData.FlagHandler {
                            override fun invoke(self: Mob) {
                                self.hearts += 1
                            }
                        })
                }
            ),
            terrainString = """
                ---*
                -*--
                ---*
            """.trimIndent()
        )
    ).associateBy { it.id }
}
