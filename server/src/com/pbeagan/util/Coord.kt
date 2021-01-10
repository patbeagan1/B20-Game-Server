package com.pbeagan.util

data class Coord(val x: Int, val y: Int) : Comparable<Coord> {
    override fun compareTo(other: Coord): Int = when {
        other.y < y -> -Util.distance(other, this)
        other.x < x -> -Util.distance(other, this)
        else -> Util.distance(other, this)
    }.toInt()
}