package com.pbeagan.actions.actiondelegates

import com.pbeagan.writer.Writer

abstract class ActionDelegateProvider<T> {
    lateinit var writer: Writer
    abstract fun build(): T
    fun prepare(writer: Writer): T {
        this.writer = writer
        return build()
    }
}
