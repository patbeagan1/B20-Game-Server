package com.pbeagan.data


import com.pbeagan.consolevision.Coord
import com.pbeagan.consolevision.Util

interface HasLocation {
    var locationInRoom: Coord
}

fun <T : HasLocation> T?.takeIfItIsInRangeOf(target: HasLocation, range: Int) =
    if (this != null && Util.distance(this.locationInRoom, target.locationInRoom).also { println(it) } <= range) this else null
