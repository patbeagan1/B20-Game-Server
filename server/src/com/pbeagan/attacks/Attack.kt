package com.pbeagan.attacks

import com.pbeagan.actions.Action
import com.pbeagan.data.Mob

abstract class Attack: Action() {
    abstract val targetList: List<Mob>
    abstract val range: Int
}