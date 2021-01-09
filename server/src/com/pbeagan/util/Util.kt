package com.pbeagan.util

import kotlin.math.absoluteValue
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt

object Util {

    fun distance(xy1: Coord, xy2: Coord) = distance(
        xy1.x.toDouble(),
        xy1.y.toDouble(),
        xy2.x.toDouble(),
        xy2.y.toDouble()
    )

    fun distanceManhattan(first: Coord, second: Coord) =
        (first.x - second.x).absoluteValue + (first.y - second.y).absoluteValue

    fun distance(xy1: Pair<Double, Double>, xy2: Pair<Double, Double>) =
        distance(xy1.first, xy1.second, xy2.first, xy2.second)

    fun distance(x1: Double, y1: Double, x2: Double, y2: Double): Double =
        sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1))

    fun toCartesian(range: Double, angle: Double): Pair<Double, Double> =
        range * cos(angle) to range * sin(angle)

    fun convertToPointAndTranslateBy(x: Int, y: Int): (Pair<Double, Double>) -> Coord = {
        Coord(x + it.first.roundToInt(), y + it.second.roundToInt())
    }

    fun pointIsInsideTriangle(s: Coord, a: Coord, b: Coord, c: Coord): Boolean {
        // https://stackoverflow.com/questions/2049582/how-to-determine-if-a-point-is-in-a-2d-triangle
        val asX: Int = s.x - a.x
        val asY: Int = s.y - a.y
        val sAB: Boolean = (b.x - a.x) * asY - (b.y - a.y) * asX > 0
        if ((c.x - a.x) * asY - (c.y - a.y) * asX > 0 == sAB) return false
        return (c.x - b.x) * (s.y - b.y) - (c.y - b.y) * (s.x - b.x) > 0 == sAB
    }
}