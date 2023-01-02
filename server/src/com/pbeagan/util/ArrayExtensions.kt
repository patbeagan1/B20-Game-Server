package com.pbeagan.util


inline fun <reified T> List2D<T>.toArray2D(): Array<Array<T>> =
    value.map { it.toTypedArray() }.toTypedArray()

inline fun <reified T> Array<Array<T>>.toList2D() = List2D(map { rows -> rows.toList() })

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