package com.pbeagan.writer

import java.util.concurrent.atomic.AtomicInteger

object IDforIOGenerator {
    private val current = AtomicInteger(0)
    fun get() = current.incrementAndGet()
}