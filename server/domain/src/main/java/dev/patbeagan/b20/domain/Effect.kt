package dev.patbeagan.b20.domain

import dev.patbeagan.b20.domain.stats.Stats

interface Effect : Stats {
    var roundsLeft: Int
    val name: String
    val descriptionActivation: String
    val descriptionDeactivation: String
    val type: Type

    enum class Type {
        ANCESTRY, SPELL
    }
}