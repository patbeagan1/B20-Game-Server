package com.pbeagan.actions.actiondelegates

import com.pbeagan.data.Mob
import com.pbeagan.writer.Writer

abstract class ActionDelegateProvider<T> {
    lateinit var writer: Writer
    abstract fun build(): T
    fun prepare(writer: Writer): T {
        this.writer = writer
        return build()
    }

    fun damageResolution(target: Mob, damage: Int) {
        writer.sayToRoomOf(target).combat("${target.name} was hit for $damage damage!")
        target.hearts -= damage
        writer.sayToRoomOf(target).combat("${target.name} is down to ${target.hearts} hp!")
        if (target.hearts < 0) {
            writer.sayToAll().dead("${target.name} has died.")
        }
    }
}
