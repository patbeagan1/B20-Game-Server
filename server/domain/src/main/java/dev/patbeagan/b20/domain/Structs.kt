package dev.patbeagan.b20.domain

enum class MessageReceiver {
    INITIATOR,
    RETALIATOR,
    ROOM
}

interface Examinable {
    val examine: String
}


