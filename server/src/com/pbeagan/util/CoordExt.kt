package com.pbeagan.util

infix fun Int.coord(other: Int) = Coord(this, other)
infix fun Coord.coord(other: Coord) = CoordRect(this, other)