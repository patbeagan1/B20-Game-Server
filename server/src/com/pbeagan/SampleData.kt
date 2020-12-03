package com.pbeagan

import com.pbeagan.mob.Mob
import com.pbeagan.models.createFlagSet

object SampleData {
    val mobs = listOf(
        Mob("Alice", baseAtkMelee = 1, armor = 3, totalHearts = 25, behavior = MobBehavior.PLAYER),
        Mob("Steve", baseAtkMelee = 1, armor = 3, totalHearts = 25, behavior = MobBehavior.PLAYER),
        Mob(
            "Bob",
            baseAtkMelee = 2,
            armor = 2,
            totalHearts = 14,
            behavior = MobBehavior.AGGRESSIVE,
            location = 2
        ),
        Mob(
            "Charlie",
            baseAtkMelee = 2,
            armor = 2,
            totalHearts = 14,
            behavior = MobBehavior.LOOTER,
            location = 3
        ),
        Mob(
            "Darla",
            baseAtkMelee = 2,
            armor = 2,
            totalHearts = 14,
            behavior = MobBehavior.WANDERER,
            location = 0
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
                    setItemFlags(ItemFlags.CONSUMABLE, object : ItemData.FlagHandler {
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
                    setItemFlags(ItemFlags.CONSUMABLE, object : ItemData.FlagHandler {
                        override fun invoke(self: Mob) {
                            self.hearts += 1
                        }
                    })
                }
            )
        )
    ).associateBy { it.id }
}
