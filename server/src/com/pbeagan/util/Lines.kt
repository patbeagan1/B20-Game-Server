package com.pbeagan.util

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.roundToInt

fun Coord.lineByDDATo(end: Coord) {
    val dx = end.x - x
    val dy = end.y - y
    val steps = max(abs(dx), abs(dy))
    val stepX = dx / steps.toFloat();
    val stepY = dy / steps.toFloat();

    var x = x.toFloat()
    var y = y.toFloat()

    (0..steps).map {
        val ret = x.roundToInt() to y.roundToInt()
        x += stepX
        y += stepY
        ret
    }.also { println(it) }
}

fun main() {
    (1 coord 1).lineByDDATo(3 coord 4)
    getCircleByBresenham(1 coord 1, 3)
}

fun MutableList<Coord>.drawCircle(x: Int, y: Int, p: Int, q: Int) = addAll(
    listOf(
        (x + p) coord (y + q),
        (x - p) coord (y + q),
        (x + p) coord (y - q),
        (x - p) coord (y - q),
        (x + q) coord (y + p),
        (x - q) coord (y + p),
        (x + q) coord (y - p),
        (x - q) coord (y - p)
    )
)

fun circleBres(xc: Int, yc: Int, r: Int): Pair<List<Coord>, CoordRect> {
    var x = 0
    var y = r
    var d = 3 - 2 * r
    val res = mutableListOf<Coord>()
    res.drawCircle(xc, yc, x, y)
    while (y >= x) {
        // for each pixel we will
        // draw all eight pixels
        x++

        // check for decision parameter
        // and correspondingly
        // update d, x, y
        d = if (d > 0) {
            y--
            d + 4 * (x - y) + 10
        } else d + 4 * x + 6
        res.drawCircle(xc, yc, x, y)
    }
    val topLeft = (xc - r) coord (yc - r)
    val botRight = (xc + r) coord (yc + r)
    return res.distinct() to (topLeft coord botRight)
}

data class Coord(val x: Int, val y: Int) : Comparable<Coord> {
    override fun compareTo(other: Coord): Int = when {
        other.y < y -> -Util.distance(other, this)
        other.x < x -> -Util.distance(other, this)
        else -> Util.distance(other, this)
    }.toInt()
}

data class CoordRect(val lesser: Coord, val greater: Coord)

infix fun Int.coord(other: Int) = Coord(this, other)
infix fun Coord.coord(other: Coord) = CoordRect(this, other)

fun List<List<Boolean>>.fillPolygon(rect: CoordRect) {
    var last = false
    var isInShape = 0
    var entering = true

    val res = traverseMap { if (it) "X" else "." }.traverseMutable()

    for (y in (rect.lesser.y + 1)..rect.greater.y) {
        for (x in rect.lesser.x..rect.greater.x) {
            if (y !in 1 until size - 1) continue
            val b = this[y][x]
            when {
                b != last -> {
                    if (isInShape == 2) entering = false
                    if (isInShape == 0) entering = true
                    if (entering) isInShape++ else isInShape--
                    if (isInShape > 0) res[y][x] = isInShape.toString()
                }
                isInShape > 0 -> res[y][x] = isInShape.toString()
            }
            last = b
        }

        isInShape = 0
        entering = true
        last = false
    }
    res.printAll("  ")
}

fun getCircleByBresenham(center: Coord, radius: Int) {
    var p = 0
    var q = radius
    var r = radius
    var decision = 3 - 2 * r
    val results = mutableListOf<Coord>()
    while (p < q) {
        results.drawCircle(center.x, center.y, p, q)
        p++
        if (decision < 0) {
            decision += 4 * p + 6
        } else {
            r -= 1
            decision += 4 * (p - q) + 10
        }
        results.drawCircle(center.x, center.y, p, q)
    }
    val sorted = results.sorted()
    (0..results.maxBy { it.y }!!.y).forEach { y ->
        (0..results.maxBy { it.x }!!.x).forEach { x ->
            if (x coord y in sorted) print("x") else print(".")
        }
        println()
    }
    println(sorted)
    println()

    circleBres(20, 20, 10).also { pair ->
        val (list, rect) = pair
        val array = Array(rect.greater.y + 1) {
            Array(rect.greater.x + 1) { false }
        }
        list.forEach { array[it.y][it.x] = true }

        array.toList2D().traverseMap { if (it) "X" else "." }.printAll("  ")

        println()

        array.toList2D().fillPolygon((10 coord 10) coord (30 coord 30))
    }
}