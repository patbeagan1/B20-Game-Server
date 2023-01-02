package com.pbeagan

import com.pbeagan.util.BooleanArray2D
import com.pbeagan.util.Coord
import com.pbeagan.util.CoordRect
import com.pbeagan.util.IntArray2D
import com.pbeagan.util.List2D
import com.pbeagan.util.addLayer
import com.pbeagan.util.coord
import com.pbeagan.util.merge
import com.pbeagan.util.mergeWith
import com.pbeagan.util.roll20
import com.pbeagan.util.roll6
import com.pbeagan.util.toArray2D
import com.pbeagan.util.toList2D
import com.pbeagan.util.traverseAdd
import com.pbeagan.util.traverseMutate
import com.pbeagan.writer.TerminalColorStyle
import com.pbeagan.writer.TerminalColorStyle.colorIntToARGB
import com.pbeagan.writer.TerminalColorStyle.style
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.roundToInt

fun Coord.lineByDDATo(end: Coord): List<Coord> {
    val dx = end.x - x
    val dy = end.y - y
    val steps = max(abs(dx), abs(dy))
    val stepX = dx / steps.toFloat();
    val stepY = dy / steps.toFloat();

    var x = x.toFloat()
    var y = y.toFloat()

    return (0..steps).map {
        val ret = x.roundToInt() coord y.roundToInt()
        x += stepX
        y += stepY
        ret
    }
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

fun Array<Array<Boolean>>.fillPolygon(
    rect: CoordRect,
    fill: Boolean = true,
): Array<Array<Boolean>> {
    var last = false
    var isInShape = 0
    var entering = true

    for (y in (rect.lesser.y + 1)..rect.greater.y) {
        for (x in rect.lesser.x..rect.greater.x) {
            if (y !in 1 until size - 1) continue
            val b = this[y][x]
            when {
                b != last -> {
                    if (isInShape == 2) entering = false
                    if (isInShape == 0) entering = true
                    if (entering) isInShape++ else isInShape--
                    if (isInShape > 0) this[y][x] = fill
                }

                isInShape > 0 -> this[y][x] = fill
            }
            last = b
        }

        isInShape = 0
        entering = true
        last = false
    }
    return this
}

fun getCircleByBresenham(center: Coord, radius: Int) {
//    var p = 0
//    var q = radius
//    var r = radius
//    var decision = 3 - 2 * r
//    val results = mutableListOf<Coord>()
//    while (p < q) {
//        results.drawCircle(center.x, center.y, p, q)
//        p++
//        if (decision < 0) {
//            decision += 4 * p + 6
//        } else {
//            r -= 1
//            decision += 4 * (p - q) + 10
//        }
//        results.drawCircle(center.x, center.y, p, q)
//    }
//    val sorted = results.sorted()
//    (0..results.maxBy { it.y }!!.y).forEach { y ->
//        (0..results.maxBy { it.x }!!.x).forEach { x ->
//            if (x coord y in sorted) print("x") else print(".")
//        }
//        println()
//    }
//    println(sorted)
//    println()

    previewCircle()

    val height = 40
    val width = 80
    val screen = IntArray2D(Array(height) { Array(width) { 0 } })
    var tick = 0
    forever(1000 / 20) {
        println(TerminalColorStyle.HIDE_CURSOR + TerminalColorStyle.RIS)
        (0..5).forEach {
            screen.addLayer((it * 192 % 256) shl 16 or 0x00AA88) {
                drawCircle(it * 7 + roll6().value coord 32 + roll20().rollSign().value, 5)
            }
        }
        screen.addLayer(0xff0000) {
            drawCircle(((tick++) % width) coord 20, 10)
        }
        screen.also { it.value.printScreenColor() }
        screen.traverseMutate { x, y, i -> 0 }
    }
}

fun forever(limiter: Int? = 100, action: () -> Unit) {
    while (true) {
        action()
        limiter?.let { Thread.sleep(it.toLong()) }
    }
}

fun List2D<Boolean>.drawCircle(center: Coord, radius: Int) =
    this.toArray2D().let { BooleanArray2D(it) }.drawCircle(center, radius)

fun BooleanArray2D.drawCircle(center: Coord, radius: Int) {
    circleBres(center.x, center.y, radius).also {
        val (list, _) = it
        traverseAdd(list, true)
    }
}

fun List2D<Boolean>.drawLine(start: Coord, end: Coord) =
    this.toArray2D().let { BooleanArray2D(it) }.drawLine(start, end)

fun BooleanArray2D.drawLine(start: Coord, end: Coord) {
    start.lineByDDATo(end).also { traverseAdd(it, true) }
}

fun List2D<Boolean>.drawCircleFilled(center: Coord, radius: Int) =
    this.toArray2D().let { BooleanArray2D(it) }.drawCircleFilled(center, radius)

fun BooleanArray2D.drawCircleFilled(center: Coord, radius: Int) {
    circleBres(center.x, center.y, radius).also {
        val (list, rect) = it
        traverseAdd(list, true)
        this.value.fillPolygon(rect.modifyBy(gy = -1), true)
    }
}

private fun previewCircle() {
    val screen = BooleanArray2D(Array(43) {
        Array(80) { false }
    })
//    circleBres(20, 20, 10).also { pair ->
//        val (list, rect) = pair
//        screen.traverseAdd(list, true)
//
//        printScreen(screen)
//
//        println()
//
//        screen.fillPolygon(rect.modifyBy(gy = -1), true)
//        printScreen(screen)
//    }

    screen.drawCircleFilled(30 coord 30, 10)
    screen.drawCircle(20 coord 20, 10)
    screen.printScreen()

    val screen2 = screen.toList2D().traverseMap { false }.toArray2D().let { BooleanArray2D(it) }
    screen2.drawCircleFilled(35 coord 35, 10)

    val merge = screen
        .mergeWith(screen2, false) { first: Boolean, second: Boolean -> first xor second }
        .toArray2D()
        .let { BooleanArray2D(it) }
    merge.printScreen()

    val screenColor = screen
        .toList2D()
        .traverseMapIndexed { x, y, t -> x shl 16 or y shl 8 }
        .toArray2D()
        .also { it.printScreenColor() }
        .let { IntArray2D(it) }

    val screenColor2 = screen
        .toList2D()
        .traverseMap { t -> if (t) 0xFF0000 else 0 }
        .toArray2D()
        .let { IntArray2D(it) }

    val also = screenColor.mergeWith(screenColor2, 0) { first: Int, second: Int ->
        if (second != 0) second else first
    }.toArray2D().let { IntArray2D(it) }.also { it.value.printScreenColor() }

    val traverseMap = screen.toList2D().traverseMap { false }.toArray2D().let { BooleanArray2D(it) }
    traverseMap.drawLine(23 coord 20, 40 coord 75)
    (also to traverseMap).merge(0) { first, second ->
        if (second) 0xffffff else first
    }.also { it.printScreenColor() }

    (0..5).forEach {
        also.addLayer((it * 192 % 256) shl 16 or 0x00AA88) {
            drawCircle(it * 7 + roll6().value coord 32 + roll20().rollSign().value, 5)
        }
    }
    also.also { it.value.printScreenColor() }

//    circleBres(40, 30, 20).also {
//        val (list, rect) = it
//        screen.traverseAdd(list, true)
//        screen.fillPolygon(rect.modifyBy(gy = -1), true)
//    }
//
//    printScreen(screen)
}

private fun BooleanArray2D.printScreen() {
    toList2D().traverseMap { t -> if (t) "XX" else ".." }.printAll("")
}

private fun Array<Array<Int>>.printScreenColor() {
    toList2D().traverseMap { t ->
        "  ".style(
            colorBackground = TerminalColorStyle.Colors.Custom(t.colorIntToARGB())
        )
    }.printAll("")
}

private fun List2D<Int>.printScreenColor() {
    traverseMap { t ->
        "  ".style(
            colorBackground = TerminalColorStyle.Colors.Custom(t.colorIntToARGB())
        )
    }.printAll("")
}