package com.pbeagan.domain.attacks.type

import com.pbeagan.domain.Mob
import com.pbeagan.domain.actions.type.Action

abstract class Attack: Action() {
    abstract val targetList: List<Mob>
    abstract val range: Int
}