package dev.patbeagan.base

interface Flag {
    val ordinal: Int
    val value get() = 1 shl (this.ordinal)
}

data class FlagSet<T : Flag>(private var value: Int) {
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

    companion object {
        fun <T : Flag> of(vararg a: T) = a
            .distinct()
            .sumOf {
                it.value
            }.let {
                FlagSet<T>(it)
            }
    }
}

enum class FlagImpl : Flag {
    A, B, C;
}

fun main() {
    val g = FlagSet.of(
        FlagImpl.A,
        FlagImpl.A,
        FlagImpl.C
    )
    println("$g ${g.contains(FlagImpl.B)}")
    g.add(FlagImpl.B)
    println("$g ${g.contains(FlagImpl.B)}")
    g.add(FlagImpl.B)
    println("$g ${g.contains(FlagImpl.B)}")
    g.remove(FlagImpl.B)
    println("$g ${g.contains(FlagImpl.B)}")
    println("$g ${g.contains(FlagImpl.A)}")
    g.remove(FlagImpl.A)
    println("$g ${g.contains(FlagImpl.A)}")
}