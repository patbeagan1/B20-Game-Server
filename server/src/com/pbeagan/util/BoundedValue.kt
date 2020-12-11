package com.pbeagan.util

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class BoundedValue<T, R : Comparable<R>, S : ClosedRange<R>>(
    initial: R,
    private val bounds: S
) : ReadWriteProperty<T, R> {
    private var innerValue: R = initial
    override fun getValue(thisRef: T, property: KProperty<*>) = innerValue
    override fun setValue(thisRef: T, property: KProperty<*>, value: R) {
        if (value in bounds) innerValue = value
    }
}