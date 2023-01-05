package dev.patbeagan.b20.domain


import com.pbeagan.consolevision.Coord
import com.pbeagan.consolevision.Util

interface HasLocation {
    var locationInRoom: Coord
}

fun <T : HasLocation> T?.takeIfItIsInRangeOf(target: HasLocation, range: Int): T? {
    if (this == null) return null
    val distance = Util.distance(this.locationInRoom, target.locationInRoom)
    println(distance)
    return if (distance <= range) this else null
}
