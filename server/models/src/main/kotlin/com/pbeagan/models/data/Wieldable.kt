package com.pbeagan.models.data

data class Wieldable(
    val armor: Int,
    val isWielded: Boolean,
    val item: Item
) : Item by item