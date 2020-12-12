package com.pbeagan.util

inline fun <reified T> Array<Array<T>>.deepClone() =
    this.map { rows -> rows.map { it }.toTypedArray() }.toTypedArray()

inline fun <reified T, reified R> Array<Array<T>>.cloneStructure(default: (T) -> R) =
    this.map { rows -> rows.map { default(it) }.toTypedArray() }.toTypedArray()

inline fun <T> Array<Array<T>>.traverse(onRowEnd: () -> Unit = {}, onElement: (T) -> Unit) {
    forEach { innerArray ->
        innerArray.forEach { onElement(it) }
        onRowEnd()
    }
}

fun <T> Array<Array<T>>.printAll() {
    traverse({ println() }) {
        print("$it\t")
    }
}