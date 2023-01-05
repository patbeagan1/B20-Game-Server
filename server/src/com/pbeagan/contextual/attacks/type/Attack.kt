package com.pbeagan.contextual.attacks.type

import com.pbeagan.contextual.Mob
import com.pbeagan.contextual.actions.type.Action

abstract class Attack: Action() {
    abstract val targetList: List<Mob>
    abstract val range: Int
}