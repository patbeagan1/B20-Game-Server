package com.pbeagan.domain.ancestry.type

import com.pbeagan.domain.actions.type.Action
import com.pbeagan.domain.Effect
import com.pbeagan.domain.Mob
import com.pbeagan.domain.Mob.MobBehavior
import com.pbeagan.stats.Stats

abstract class Ancestry(private val stats: Stats) : Effect, Stats by stats {
    override var roundsLeft: Int = Int.MAX_VALUE
    override var type: Effect.Type = Effect.Type.ANCESTRY
    override var name: String = "${this::class.java.simpleName} ancestry"
    override var descriptionActivation: String = ""
    override var descriptionDeactivation: String = ""

    abstract fun decide(mob: Mob, behavior: MobBehavior): Action
}
