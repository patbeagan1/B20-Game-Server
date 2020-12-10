package com.pbeagan.ancestry

import com.pbeagan.actions.Action
import com.pbeagan.data.Effect
import com.pbeagan.data.Mob
import com.pbeagan.data.MobBehavior
import com.pbeagan.data.Stats

abstract class Ancestry(private val stats: Stats) : Effect, Stats by stats {
    override var roundsLeft: Int = Int.MAX_VALUE
    override var type: Effect.Type = Effect.Type.ANCESTRY
    override var name: String = "${this::class.java.simpleName} ancestry"
    override var descriptionActivation: String = ""
    override var descriptionDeactivation: String = ""

    abstract fun decide(mob: Mob, behavior: MobBehavior): Action
}
