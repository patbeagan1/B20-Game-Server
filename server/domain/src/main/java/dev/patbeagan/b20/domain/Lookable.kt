package dev.patbeagan.b20.domain

import dev.patbeagan.b20.domain.types.Direction

interface Lookable {
    val descriptionLook: String
    val descriptionPreview: (Direction) -> String
}