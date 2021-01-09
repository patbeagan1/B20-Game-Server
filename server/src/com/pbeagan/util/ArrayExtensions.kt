package com.pbeagan.util

typealias  List2D<T> = Iterable<Iterable<T>>

inline fun <reified T> List2D<T>.toArray2D() = map { rows -> rows.map { it }.toTypedArray() }.toTypedArray()
inline fun <reified T> Array<Array<T>>.toList2D() = map { rows -> rows.map { it } }

inline fun <reified T, reified R> Array<Array<T>>.cloneStructure(default: (T) -> R) =
    this.map { rows -> rows.map { default(it) }.toTypedArray() }.toTypedArray()

inline fun <T> List2D<T>.traverse(onRowEnd: () -> Unit = {}, onElement: (T) -> Unit) {
    forEach { innerArray ->
        innerArray.forEach { onElement(it) }
        onRowEnd()
    }
}

inline fun <T, R> List2D<T>.traverseMap(onElement: (T) -> R): List<List<R>> =
    map { r -> r.map { onElement(it) } }

fun <T> List2D<T>.traverseMutable(): MutableList<MutableList<T>> = map { it.toMutableList() }.toMutableList()


inline fun <T> List2D<T>.traverseIndexed(onRowEnd: () -> Unit = {}, onElement: (T, Int, Int) -> Unit) {
    forEachIndexed { y, innerArray ->
        innerArray.forEachIndexed { x, t -> onElement(t, x, y) }
        onRowEnd()
    }
}

inline fun <reified T> List2D<T>.printAll(delimiter: String = "\t") {
    traverse({ println() }) { print("$it$delimiter") }
}