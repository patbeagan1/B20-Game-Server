package dev.patbeagan.base

import java.util.concurrent.atomic.AtomicInteger

object UniqueId {
    private val current = AtomicInteger(0)
    fun get() = current.incrementAndGet()
}