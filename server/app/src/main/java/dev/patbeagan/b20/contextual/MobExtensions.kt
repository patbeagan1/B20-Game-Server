package com.pbeagan.contextual

import com.pbeagan.data.writer.Writer
import dev.patbeagan.b20.WorldState
import dev.patbeagan.b20.contextual.Mob
import dev.patbeagan.b20.domain.stats.AttackValue

fun Mob.formatHP() = "${this.nameStyled}(${this.hearts} hp)"

fun Mob.changeHealth(damage: AttackValue, worldState: WorldState, writer: Writer) {
    writer.sayToRoomOf(this).combat("$nameStyled was hit for $damage damage!")
    hearts -= damage.value
    writer.sayToRoomOf(this).combat("$nameStyled is down to $hearts hp!")
    if (hearts <= 0) {
        writer.sayToAll().dead("$nameStyled has died.")
        die(writer, worldState)
    }
}