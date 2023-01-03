package com.pbeagan.util

inline fun <reified T> Array<Array<T>>.toList2D() = List2D.from(map { rows -> rows.toList() })

inline fun <reified T, reified S, reified R> Pair<List2D<T>, List2D<S>>.merge(
    default: R,
    crossinline onElement: (first: T, second: S) -> R,
): List2D<R> = first.mergeWith(second, default, onElement)

inline fun <reified T, reified S, reified R> List2D<T>.mergeWith(
    other: List2D<S>,
    default: R,
    crossinline onElement: (first: T, second: S) -> R,
): List2D<R> = this.traverseMapIndexed { x, y, t ->
    if (other.isValidCoordinate(x coord y)) {
        onElement(this.at(x, y), other.at(x, y))
    } else {
        default
    }
}

fun List2D<Int>.addLayer(
    color: Int,
    config: List2D<Boolean>.() -> Unit,
) {
    val other = traverseMap { false }.also(config)
    this.traverseMutate { x, y, each ->
        if (other.isValidCoordinate(x coord y)) {
            if (other.at(x, y)) return@traverseMutate color
        }
        return@traverseMutate each
    }
}