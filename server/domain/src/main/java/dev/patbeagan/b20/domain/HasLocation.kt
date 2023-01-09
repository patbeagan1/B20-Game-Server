package dev.patbeagan.b20.domain

import dev.patbeagan.consolevision.types.CompressedPoint
import dev.patbeagan.consolevision.util.distance

interface HasLocation {
    var locationInRoom: CompressedPoint
}

fun <T : HasLocation> T?.takeIfItIsInRangeOf(target: HasLocation, range: Int): T? {
    if (this == null) return null
    val distance = distance(
        this.locationInRoom.x.toDouble(),
        this.locationInRoom.y.toDouble(),
        target.locationInRoom.x.toDouble(),
        target.locationInRoom.y.toDouble()
    )
    println(distance)
    return if (distance <= range) this else null
}
