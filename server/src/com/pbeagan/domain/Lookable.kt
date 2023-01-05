package com.pbeagan.domain

import com.pbeagan.domain.types.Direction

interface Lookable {
    val descriptionLook: String
    val descriptionPreview: (Direction) -> String
}