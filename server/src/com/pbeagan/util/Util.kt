package com.pbeagan.util

import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt

object Util {

    fun distanceInt(xy1: Pair<Int, Int>, xy2: Pair<Int, Int>) = distance(
        xy1.first.toDouble(),
        xy1.second.toDouble(),
        xy2.first.toDouble(),
        xy2.second.toDouble()
    )

    fun distance(xy1: Pair<Double, Double>, xy2: Pair<Double, Double>) =
        distance(xy1.first, xy1.second, xy2.first, xy2.second)

    fun distance(x1: Double, y1: Double, x2: Double, y2: Double): Double =
        sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1))

    fun intRange(coord: Int, range: Int) = (coord - range)..(coord + range)

    fun toCartesian(range: Double, angle: Double): Pair<Double, Double> =
        range * cos(angle) to range * sin(angle)

    fun convertToPointAndTranslateBy(x: Int, y: Int): (Pair<Double, Double>) -> Point = {
        Point(x + it.first.roundToInt(), y + it.second.roundToInt())
    }

    fun pointIsInsideTriangle(s: Point, a: Point, b: Point, c: Point): Boolean {
        // https://stackoverflow.com/questions/2049582/how-to-determine-if-a-point-is-in-a-2d-triangle
        val asX: Int = s.x - a.x
        val asY: Int = s.y - a.y
        val sAB: Boolean = (b.x - a.x) * asY - (b.y - a.y) * asX > 0
        if ((c.x - a.x) * asY - (c.y - a.y) * asX > 0 == sAB) return false
        return (c.x - b.x) * (s.y - b.y) - (c.y - b.y) * (s.x - b.x) > 0 == sAB
    }
}