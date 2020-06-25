package com.pbeagan.models.data

data class Location(var x: Double, var y: Double) {
    companion object {
        fun random(): Location =
            Location(
                (0..100).random().toDouble(),
                (0..100).random().toDouble()
            )
    }
}