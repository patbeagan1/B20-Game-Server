package com.pbeagan.util

import java.util.concurrent.atomic.AtomicInteger

object UniqueId {
    private val current = AtomicInteger(0)
    fun get() = current.incrementAndGet()
}