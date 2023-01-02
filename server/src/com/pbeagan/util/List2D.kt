package com.pbeagan.util

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