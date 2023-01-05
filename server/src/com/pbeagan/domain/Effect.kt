package com.pbeagan.domain

import com.pbeagan.stats.Stats

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