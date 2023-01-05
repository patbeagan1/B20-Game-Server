package com.pbeagan.domain.actions

import com.pbeagan.domain.actions.type.Action
import com.pbeagan.domain.actions.type.FreeAction
import com.pbeagan.domain.Mob

class Retry(private val errorMsg: String) : Action(),
    FreeAction {
    override operator fun invoke(self: Mob) = writer.sayTo(self).error(errorMsg)
}