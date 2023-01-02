package com.pbeagan.util

interface Array2D<T> {
    val value: Array<Array<T>>
}

@JvmInline
value class IntArray2D(override val value: Array<Array<Int>>) : Array2D<Int>

@JvmInline
value class BooleanArray2D(override val value: Array<Array<Boolean>>) : Array2D<Boolean>

inline fun <T> Array2D<T>.traverseMutate(
    onElement: (x: Int, y: Int, each: T) -> T,
): Unit = value.forEachIndexed { y, row ->
    row.forEachIndexed { x, t -> value[y][x] = onElement(x, y, t) }
}

inline fun <reified T> Array2D<T>.toList2D(): List2D<T> = value.toList2D()