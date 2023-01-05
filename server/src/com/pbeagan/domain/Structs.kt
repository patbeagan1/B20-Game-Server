package com.pbeagan.domain

enum class MessageReceiver {
    INITIATOR,
    RETALIATOR,
    ROOM
}

interface Examinable {
    val examine: String
}


