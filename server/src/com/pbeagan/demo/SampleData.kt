package com.pbeagan.demo

import com.pbeagan.ancestry.Goblin
import com.pbeagan.ancestry.Hobgoblin
import com.pbeagan.ancestry.Human
import com.pbeagan.data.DefenseValue
import com.pbeagan.data.Direction.DOWN
import com.pbeagan.data.Direction.EAST
import com.pbeagan.data.Direction.NORTH
import com.pbeagan.data.Direction.SOUTH
import com.pbeagan.data.Direction.UP
import com.pbeagan.data.Direction.WEST
import com.pbeagan.data.Exits
import com.pbeagan.data.ItemData
import com.pbeagan.data.ItemFlags
import com.pbeagan.data.Lighting
import com.pbeagan.data.Mob
import com.pbeagan.data.MobBehavior
import com.pbeagan.data.RoomData
import com.pbeagan.data.RoomDirectionData
import com.pbeagan.data.RoomFlags
import com.pbeagan.util.coord
import com.pbeagan.util.createFlagSet


object SampleData {
    val mobs = listOf(
        Mob(
            "Alice",
//            armor = 3,
            effects = listOf(Human()),
            isPlayer = true,
            description = object : Mob.Description {},
            locationInRoom = 2 coord 3
        ),
        Mob(
            "Steve",
            armor = DefenseValue(3),
            isPlayer = true,
            effects = listOf(Human()),
            description = object : Mob.Description {},
            locationInRoom = 2 coord 4
        ),
        Mob(
            "Bob",
            armor = DefenseValue(2),
            effects = listOf(Goblin()),
            behavior = MobBehavior.AGGRESSIVE,
            location = 2,
            description = object : Mob.Description {},
            locationInRoom = 5 coord 5
        ),
        Mob(
            "Charlie",
            armor = DefenseValue(2),
            effects = listOf(Hobgoblin()),
            behavior = MobBehavior.LOOTER,
            location = 3,
            description = object : Mob.Description {},
            locationInRoom = 1 coord 1
        ),
        Mob(
            "Darla",
            armor = DefenseValue(2),
            effects = listOf(Goblin()),
            behavior = MobBehavior.WANDERER,
            location = 0,
            description = object : Mob.Description {},
            locationInRoom = 5 coord 5
        )
    )
    val rooms = listOf(
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
            roomFlags = createFlagSet(RoomFlags.INDOORS),
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
