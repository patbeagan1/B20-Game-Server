package com.pbeagan.util

import com.pbeagan.data.ValueContainer
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
inline fun <T : Any> ifLet(vararg elements: T?, closure: (List<T>) -> Unit) {
    if (elements.all { it != null }) {
        closure(elements.filterNotNull())
    }
}