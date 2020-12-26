package com.pbeagan.actions

import com.pbeagan.data.Mob

object Inactive : Action() {
    override operator fun invoke(self: Mob) = writer.sayToRoomOf(self).info("${self.nameStyled} is inactive")
}