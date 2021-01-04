package com.pbeagan.data

import com.pbeagan.util.Util

interface HasLocation {
    var locationInRoom: Pair<Int, Int>
}

fun <T : HasLocation> T?.takeIfItIsInRangeOf(target: HasLocation, range: Int) =
    if (this != null && Util.distanceInt(this.locationInRoom, target.locationInRoom).also { println(it) } <= range) this else null
