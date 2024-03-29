package dev.patbeagan.b20.util

import dev.patbeagan.consolevision.types.CompressedPoint
import dev.patbeagan.consolevision.types.coord
import dev.patbeagan.consolevision.util.convertToPointAndTranslateBy
import dev.patbeagan.consolevision.util.toCartesian
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
        range: Int,
    ): HashSet<CompressedPoint> =
        y.rangeBy(range).flatMap { ly ->
            x.rangeBy(range).map { lx -> lx coord ly }
        }.filter {
            (x coord y).distanceFrom(it) < range + 1
        }.toHashSet()

    fun checkVisionRange(x: Int, y: Int, range: Double, angleLeft: Double, angleRight: Double) {
        val v1 = CompressedPoint.from(x, y)
        val v2 = toCartesian(range, angleLeft)
            .let(convertToPointAndTranslateBy(x, y))
        val v3 = toCartesian(range, angleRight)
            .let(convertToPointAndTranslateBy(x, y))
        val minOfY = minOf(v1.y, v2.y, v3.y)
        val maxOfY = maxOf(v1.y, v2.y, v3.y)
        val minOfX = minOf(v1.x, v2.x, v3.x)
        val maxOfX = maxOf(v1.x, v2.x, v3.x)
        (minOfY..maxOfY).flatMap { ly ->
            (minOfX..maxOfX).map { lx -> CompressedPoint.from(lx, ly) }
        }.filter {
            it.isInTriangle(v1, v2, v3)
        }.also { points ->
            println("$v1, $v2, $v3")
            print(points)
            debugLocalRange(y, range.toInt(), x, points.map { it.x coord it.y })
        }
    }

    private fun debugLocalRange(y: Int, range: Int, x: Int, filter: Collection<CompressedPoint>) {
        y.rangeBy(range).flatMap { ly ->
            println()
            x.rangeBy(range).map { lx ->
                if (lx coord ly in filter) print("X") else print("-")
            }
        }
        println()
    }
}