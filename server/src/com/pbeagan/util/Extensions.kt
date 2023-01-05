package com.pbeagan.util

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.Locale

val Any.exhaustive get() = this.let {}

fun String.commonPrefixWithIgnoreCase(prefix: String) =
    this.commonPrefixWith(prefix.lowercase(Locale.getDefault()), ignoreCase = true).equals(prefix, ignoreCase = true)

fun Int.rangeBy(range: Int) = (this - range)..(this + range)

fun loggerGen(clazz: Class<out Any>): Logger =
    LoggerFactory.getLogger(clazz)

inline fun <T : Any> guardLet(
    vararg elements: T?,
    closure: () -> Nothing
): List<T> = if (elements.all { it != null }) {
    elements.filterNotNull()
} else {
    closure()
}

inline fun <T : Any> ifLet(vararg elements: T?, closure: (List<T>) -> Unit) {
    if (elements.all { it != null }) {
        closure(elements.filterNotNull())
    }
}