package com.pbeagan.util

import kotlin.math.PI

fun main() {
    Senses.checkLocalRange(5, 5, 4)
        .forEach { print("${it.x}:${it.y},") }
    println()
    (0..50).forEach {
        val angleRight = 2 * PI * it / 50
        Senses.checkVisionRange(24, 24, 24.0, angleRight - PI / 4, angleRight)
    }
}

object Senses {
    fun checkLocalRange(
        x: Int,
        y: Int,
        range: Int
    ): HashSet<Coord> =
        y.rangeBy(range).flatMap { ly ->
            x.rangeBy(range).map { lx -> lx coord ly }
        }.filter {
            Util.distance(
                x coord y,
                it
            ) < range + 1
        }.toHashSet()

    fun checkVisionRange(x: Int, y: Int, range: Double, angleLeft: Double, angleRight: Double) {
        val v1 = Coord(x, y)
        val v2 = Util.toCartesian(range, angleLeft)
            .let(Util.convertToPointAndTranslateBy(x, y))
        val v3 = Util.toCartesian(range, angleRight)
            .let(Util.convertToPointAndTranslateBy(x, y))
        val minOfY = minOf(v1.y, v2.y, v3.y)
        val maxOfY = maxOf(v1.y, v2.y, v3.y)
        val minOfX = minOf(v1.x, v2.x, v3.x)
        val maxOfX = maxOf(v1.x, v2.x, v3.x)
        (minOfY..maxOfY).flatMap { ly ->
            (minOfX..maxOfX).map { lx -> Coord(lx, ly) }
        }.filter {
            Util.pointIsInsideTriangle(it, v1, v2, v3)
        }.also { points ->
            println("$v1, $v2, $v3")
            print(points)
            debugLocalRange(y, range.toInt(), x, points.map { it.x coord it.y })
        }
    }

    private fun debugLocalRange(y: Int, range: Int, x: Int, filter: Collection<Coord>) {
        y.rangeBy(range).flatMap { ly ->
            println()
            x.rangeBy(range).map { lx ->
                if (lx coord ly in filter) print("X") else print("-")
            }
        }
        println()
    }
}