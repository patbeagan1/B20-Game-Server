package com.pbeagan.contextual.actions

import com.pbeagan.contextual.Mob
import com.pbeagan.contextual.actions.type.Action

object Inactive : Action() {
    override operator fun invoke(self: Mob) = writer.sayToRoomOf(self).info("${self.nameStyled} is inactive")
}