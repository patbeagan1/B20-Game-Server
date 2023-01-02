package com.pbeagan.util


@JvmInline
value class IntArray2D(override val value: Array<Array<Int>>) : Array2D<Int>

@JvmInline
value class BooleanArray2D(override val value: Array<Array<Boolean>>) : Array2D<Boolean>

interface Array2D<T> {
    val value: Array<Array<T>>
}

inline fun <T> Array2D<T>.traverseMutate(
    onElement: (x: Int, y: Int, each: T) -> T,
): Unit = value.forEachIndexed { y, row ->
    row.forEachIndexed { x, t -> value[y][x] = onElement(x, y, t) }
}

inline fun <reified T> Array2D<T>.toList2D(): List2D<T> = value.toList2D()
inline fun <reified T> List2D<T>.toArray2D(): Array<Array<T>> =
    value.map { it.toTypedArray() }.toTypedArray()

inline fun <reified T> Array<Array<T>>.toList2D() = List2D(map { rows -> rows.toList() })

@JvmInline
value class List2D<T>(val value: List<List<T>>) : Iterable<T> {

    val height get() = value.size
    val width get() = value.firstOrNull()?.size ?: 0

    fun at(x: Int, y: Int): T = value[y][x]

    override fun iterator(): Iterator<T> = iterator {
        this@List2D.traverse { _, _, t -> yield(t) }
    }

    inline fun <reified R> traverseMapIndexed(
        crossinline onElement: (x: Int, y: Int, T) -> R,
    ): List2D<R> =
        List2D(value.mapIndexed { y, r -> r.mapIndexed { x, it -> onElement(x, y, it) } })

    inline fun <reified R> traverseMap(
        crossinline onElement: (T) -> R,
    ): List2D<R> = traverseMapIndexed { _, _, t -> onElement(t) }

    inline fun traverse(
        onRowEnd: () -> Unit = {},
        onElement: (x: Int, y: Int, T) -> Unit,
    ): Unit = value.forEachIndexed { y, r ->
        r.forEachIndexed { x, t -> onElement(x, y, t) }
        onRowEnd()
    }

    fun isValidCoordinate(c: Coord): Boolean =
        value.isNotEmpty() &&
                value.all { it.size == value[0].size } &&
                c.y in 0..value.size &&
                c.x in 0..value[0].size

    fun printAll(delimiter: String = "\t") {
        traverse({ println() }) { _, _, t -> print("$t$delimiter") }
    }

    fun flatten(): List<T> {
        val ret = mutableListOf<T>()
        iterator().forEach { ret.add(it) }
        return ret
    }
}

inline fun <reified T, reified S, reified R> Pair<Array2D<T>, Array2D<S>>.merge(
    default: R,
    crossinline onElement: (first: T, second: S) -> R,
): List2D<R> = first.mergeWith(second, default, onElement)

inline fun <reified T, reified S, reified R> Array2D<T>.mergeWith(
    other: Array2D<S>,
    default: R,
    crossinline onElement: (first: T, second: S) -> R,
): List2D<R> {
    val us = this.toList2D()
    val them = other.toList2D()
    return us.traverseMapIndexed { x, y, t ->
        if (them.isValidCoordinate(x coord y)) {
            onElement(us.at(x, y), them.at(x, y))
        } else {
            default
        }
    }
}

fun <T> Array2D<T>.traverseAdd(list: List<Coord>, t: T) {
    list.forEach {
        if (it.y in this.value.indices && it.x in this.value[0].indices) {
            this.value[it.y][it.x] = t
        }
    }
}

fun IntArray2D.addLayer(
    color: Int,
    config: List2D<Boolean>.() -> Unit,
) {
    val other = this.toList2D().traverseMap { false }.also(config)
    this.traverseMutate { x, y, each ->
        if (other.isValidCoordinate(x coord y)) {
            if (other.at(x, y)) return@traverseMutate color
        }
        return@traverseMutate each
    }
}