package com.pbeagan.data

enum class MobBehavior(val descriptionDefault: String) {
    HELPFUL("Seems to want to help."),
    IMMOBILE("It's rooted in place!"),
    LOOTER("It scours the area for items of value"),
    AGGRESSIVE("It looks angry..."),
    WANDERER("It is just passing through."),
    FLEE("It wishes it was someplace else."),
    SLEEPING("Is is fast asleep."),
    FOLLOW("It is following something."),
    WAITING("It is waiting for something to happen.")
}