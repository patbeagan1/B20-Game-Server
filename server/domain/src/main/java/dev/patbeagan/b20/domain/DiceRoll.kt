package dev.patbeagan.b20.domain

import dev.patbeagan.b20.domain.stats.ValueContainer
import kotlin.random.Random

@JvmInline
value class DiceRoll(val value: Int) : Comparable<ValueContainer<Int>> {
    override fun compareTo(other: ValueContainer<Int>): Int = value - other.value
    operator fun plus(a: Int) = value + a
    fun rollSign() = DiceRoll(value * if (Random.nextBoolean()) 1 else -1)
}

infix fun DiceRoll.isIn(a: IntRange) = value in a
fun roll6() = DiceRoll(Random.nextInt(1, 6))
fun roll8() = DiceRoll(Random.nextInt(1, 8))
fun roll12() = DiceRoll(Random.nextInt(1, 12))
fun roll20() = DiceRoll(Random.nextInt(1, 20))
