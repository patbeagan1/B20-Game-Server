package com.pbeagan.data

interface Lookable {
    val descriptionLook: String
    val descriptionPreview: (Direction) -> String
}