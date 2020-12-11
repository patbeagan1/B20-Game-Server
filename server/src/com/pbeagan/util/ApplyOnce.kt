package com.pbeagan.util

import java.util.concurrent.atomic.AtomicBoolean

typealias ApplyOnce = ApplyOnceWith<Unit>

class ApplyOnceWith<T>(private val action: T.() -> Unit) {
    var hasInvoked = AtomicBoolean(false)
    operator fun invoke(t: T) {
        if (!hasInvoked.get()) {
            hasInvoked.set(true)
            t.action()
        }
    }
}