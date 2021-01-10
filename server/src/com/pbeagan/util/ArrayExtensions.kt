package com.pbeagan.util

inline fun <reified T> List<List<T>>.toArray2D() = map { rows -> rows.toTypedArray() }.toTypedArray()
inline fun <reified T> Array<Array<T>>.toList2D() = map { rows -> rows.toList() }

//inline fun <reified T, reified R> Array<Array<T>>.cloneStructure(default: (T) -> R) =
//    (0..this.size).forEach { y -> (0..this[y].size).forEach { this[y][it] = default(it) } }

//inline fun <T> List2D<T>.traverse(onRowEnd: () -> Unit = {}, onElement: (T) -> Unit) {
//    forEach { innerArray ->
//        innerArray.forEach { onElement(it) }
//        onRowEnd()
//    }
//}

inline fun <T, reified R> Array<Array<T>>.traverseMapIndexed(
    onElement: (x: Int, y: Int, T) -> R
): Array<Array<R>> = mapIndexed { y, r -> r.mapIndexed { x, it -> onElement(x, y, it) } }.toArray2D()

inline fun <T, reified R> Array<Array<T>>.traverseMap(
    onElement: (T) -> R
): Array<Array<R>> = traverseMapIndexed { _, _, t -> onElement(t) }


inline fun <T> Array<Array<T>>.traverse(onRowEnd: () -> Unit = {}, onElement: (x: Int, y: Int, T) -> Unit) {
    forEachIndexed { y, r ->
        r.forEachIndexed { x, t -> onElement(x, y, t) }
        onRowEnd()
    }
}

fun <T> Array<Array<T>>.containsCoord(c: Coord): Boolean =
    this.isNotEmpty() &&
            this.all { it.size == this[0].size } &&
            c.y in 0..this.size &&
            c.x in 0..this[0].size

inline fun <reified T, reified S, reified R> Pair<Array<Array<T>>, Array<Array<S>>>.merge(
    default: R,
    onElement: (first: T, second: S) -> R
) = first.mapIndexed { y, r ->
    r.mapIndexed { x, t ->
        if (second.containsCoord(x coord y)) onElement(first[y][x], second[y][x]) else default
    }
}.toArray2D()

fun Array<Array<Int>>.addLayer(
    color: Int,
    config: Array<Array<Boolean>>.() -> Unit
) {
    val other = this.traverseMap { false }.also(config)
    this.forEachIndexed { y, r ->
        r.forEachIndexed { x, t ->
            if (other.containsCoord(x coord y)) {
                if (other[y][x]) this[y][x] = color
            }
        }
    }
}

fun <T> Array<Array<T>>.traverseAdd(list: List<Coord>, t: T) {
    list.forEach {
        if (it.y in this.indices && it.x in this[0].indices) {
            this[it.y][it.x] = t
        }
    }
}

inline fun <reified T> Array<Array<T>>.printAll(delimiter: String = "\t") {
    traverse({ println() }) { _, _, t -> print("$t$delimiter") }
}