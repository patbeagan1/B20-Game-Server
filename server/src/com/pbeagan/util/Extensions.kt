package com.pbeagan.util

import com.pbeagan.data.ValueContainer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.random.Random

inline class DiceRoll(val value: Int) : Comparable<ValueContainer<Int>> {
    override fun compareTo(other: ValueContainer<Int>): Int = value - other.value
    operator fun plus(a: Int) = value + a
    fun rollSign() = DiceRoll(value * if (Random.nextBoolean()) 1 else -1)
}

infix fun DiceRoll.isIn(a: IntRange) = value in a

fun roll6() = DiceRoll(Random.nextInt(1, 6))
fun roll8() = DiceRoll(Random.nextInt(1, 8))
fun roll12() = DiceRoll(Random.nextInt(1, 12))
fun roll20() = DiceRoll(Random.nextInt(1, 20))

val Any.exhaustive get() = this.let {}

fun String.startsWith(prefix: String) =
    this.commonPrefixWith(prefix.toLowerCase(), ignoreCase = true).equals(prefix, ignoreCase = true)

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

inline fun <T1 : Any, R : Any> safeLet(
    p1: T1?,
    block: (T1) -> R?
): R? = if (p1 != null) block(p1) else null

inline fun <T1 : Any, T2 : Any, R : Any> safeLet(
    p1: T1?,
    p2: T2?,
    block: (T1, T2) -> R?
): R? = if (p1 != null && p2 != null) block(p1, p2) else null

inline fun <T1 : Any, T2 : Any, T3 : Any, R : Any> safeLet(
    p1: T1?,
    p2: T2?,
    p3: T3?,
    block: (T1, T2, T3) -> R?
): R? =
    if (p1 != null && p2 != null && p3 != null) block(p1, p2, p3) else null

inline fun <T1 : Any, T2 : Any, T3 : Any, T4 : Any, R : Any> safeLet(
    p1: T1?,
    p2: T2?,
    p3: T3?,
    p4: T4?,
    block: (T1, T2, T3, T4) -> R?
): R? = if (
    p1 != null &&
    p2 != null &&
    p3 != null &&
    p4 != null
) block(p1, p2, p3, p4) else null

inline fun <T1 : Any, T2 : Any, T3 : Any, T4 : Any, T5 : Any, R : Any> safeLet(
    p1: T1?,
    p2: T2?,
    p3: T3?,
    p4: T4?,
    p5: T5?,
    block: (T1, T2, T3, T4, T5) -> R?
): R? = if (
    p1 != null &&
    p2 != null &&
    p3 != null &&
    p4 != null &&
    p5 != null
) block(p1, p2, p3, p4, p5) else null

inline fun <T1 : Any, T2 : Any, T3 : Any, T4 : Any, T5 : Any, T6 : Any, R : Any> safeLet(
    p1: T1?,
    p2: T2?,
    p3: T3?,
    p4: T4?,
    p5: T5?,
    p6: T6?,
    block: (T1, T2, T3, T4, T5, T6) -> R?
): R? = if (
    p1 != null &&
    p2 != null &&
    p3 != null &&
    p4 != null &&
    p5 != null &&
    p6 != null
) block(p1, p2, p3, p4, p5, p6) else null
