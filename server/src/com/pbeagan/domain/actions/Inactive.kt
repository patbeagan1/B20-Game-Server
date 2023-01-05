package com.pbeagan.domain.actions

import com.pbeagan.domain.actions.type.Action
import com.pbeagan.domain.Mob

object Inactive : Action() {
    override operator fun invoke(self: Mob) = writer.sayToRoomOf(self).info("${self.nameStyled} is inactive")
}