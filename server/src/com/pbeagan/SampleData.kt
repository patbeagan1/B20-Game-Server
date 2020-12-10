package com.pbeagan

import com.pbeagan.data.Direction
import com.pbeagan.ancestry.Goblin
import com.pbeagan.ancestry.Hobgoblin
import com.pbeagan.ancestry.Human
import com.pbeagan.data.ItemData
import com.pbeagan.data.ItemFlags
import com.pbeagan.data.Mob
import com.pbeagan.data.MobBehavior
import com.pbeagan.data.RoomData
import com.pbeagan.data.RoomDirectionData
import com.pbeagan.data.RoomFlags
import com.pbeagan.models.createFlagSet

object SampleData {
    val mobs = listOf(
        Mob(
            "Alice",
            armor = 3,
            effects = listOf(Human()),
            isPlayer = true,
            description = object : Mob.Description {}
        ),
        Mob(
            "Steve",
            armor = 3,
            isPlayer = true,
            effects = listOf(Human()),
            description = object : Mob.Description {}
        ),
        Mob(
            "Bob",
            armor = 2,
            effects = listOf(Goblin()),
            behavior = MobBehavior.AGGRESSIVE,
            location = 2,
            description = object : Mob.Description {}
        ),
        Mob(
            "Charlie",
            armor = 2,
            effects = listOf(Hobgoblin()),
            behavior = MobBehavior.LOOTER,
            location = 3,
            description = object : Mob.Description {}
        ),
        Mob(
            "Darla",
            armor = 2,
            effects = listOf(Goblin()),
            behavior = MobBehavior.WANDERER,
            location = 0,
            description = object : Mob.Description {}
        )
    )
    val rooms = listOf(
        RoomData(
            0,
            name = "The Front Porch",
            descriptionLook = "The front porch of Liz's house",
            directions = listOf(
                RoomDirectionData(
                    Direction.EAST,
                    1,
                    "The Road"
                ),
                RoomDirectionData(
                    Direction.WEST,
                    3,
                    "There is a door leading inside"
                )
            ),
            roomFlags = createFlagSet(RoomFlags.INDOORS),
            items = mutableListOf(
                ItemData(
                    0,
                    listOf("Rusty Sword", "sword"),
                    "It's got a few notches in it.",
                    "A rusty sword is lying on the ground."
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
                    descriptionInRoom = "There is an apple laying on the ground"
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
            directions = listOf(
                RoomDirectionData(
                    Direction.WEST,
                    0,
                    "The safest place around"
                ),
                RoomDirectionData(
                    Direction.NORTH,
                    2,
                    "In the distance you see an ice field."
                )
            )
        ),
        RoomData(
            2,
            name = "A bridge across the fjord",
            descriptionLook = "A sturdy metal bridge spans the gap between two fissures in an icy field.",
            directions = listOf(
                RoomDirectionData(
                    Direction.SOUTH,
                    1,
                    "There is a road to the south."
                )
            )
        ), RoomData(
            3,
            name = "The Kitchen",
            descriptionLook = "A metal table is laden with various fruits and vegetables.",
            directions = listOf(
                RoomDirectionData(
                    Direction.EAST,
                    0,
                    "There is a door leading outside"
                )
            ),
            items = mutableListOf(
                ItemData(
                    1,
                    listOf(
                        "Red Apple", "fruit", "apple", "food"
                    ),
                    descriptionOnExamination = "A polished red apple. It looks good enough to eat!",
                    descriptionInRoom = "There is an apple in a basket on the table"
                ).apply {
                    setItemFlags(
                        ItemFlags.CONSUMABLE,
                        object : ItemData.FlagHandler {
                            override fun invoke(self: Mob) {
                                self.hearts += 1
                            }
                        })
                }
            )
        )
    ).associateBy { it.id }
}
