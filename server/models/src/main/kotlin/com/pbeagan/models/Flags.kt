package com.pbeagan.models

interface Flag {
    val ordinal: Int
    val value get() = 1 shl (this.ordinal)
}

fun <T : Flag> createFlagSet(vararg a: T) = a.distinct().sumBy { it.value }.let { FlagCombined<T>(it) }
data class FlagCombined<T : Flag>(private var value: Int) {
    fun contains(a: T) = (this.value and a.value) == a.value
    fun add(a: T) {
        if (!contains(a)) this.value += a.value
    }

    fun add(vararg a: T) {
        a.forEach { add(it) }
    }

    fun remove(a: T) {
        if (contains(a)) this.value -= a.value
    }

    fun remove(vararg a: T) {
        a.forEach { remove(it) }
    }
}

enum class FlagImpl : Flag {
    A, B, C;
}

fun main() {
    val g = createFlagSet(FlagImpl.A, FlagImpl.A, FlagImpl.C)
    println("${g} ${g.contains(FlagImpl.B)}")
    g.add(FlagImpl.B)
    println("${g} ${g.contains(FlagImpl.B)}")
    g.add(FlagImpl.B)
    println("${g} ${g.contains(FlagImpl.B)}")
    g.remove(FlagImpl.B)
    println("${g} ${g.contains(FlagImpl.B)}")
}