package com.pbeagan.contextual.actions

import com.pbeagan.contextual.Mob
import com.pbeagan.contextual.actions.type.Action
import com.pbeagan.contextual.actions.type.FreeAction

class Retry(private val errorMsg: String) : Action(),
    FreeAction {
    override operator fun invoke(self: Mob) = writer.sayTo(self).error(errorMsg)
}