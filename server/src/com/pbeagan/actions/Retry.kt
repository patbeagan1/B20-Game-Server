package com.pbeagan.actions

import com.pbeagan.data.Mob

class Retry(private val errorMsg: String) : Action(),
    FreeAction {
    override operator fun invoke(self: Mob) = writer.sayTo(self).error(errorMsg)
}