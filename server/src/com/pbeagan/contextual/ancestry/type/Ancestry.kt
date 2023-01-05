package com.pbeagan.contextual.ancestry.type

import com.pbeagan.WorldState
import com.pbeagan.contextual.actions.type.Action
import com.pbeagan.contextual.Mob
import com.pbeagan.contextual.Mob.MobBehavior
import dev.patbeagan.b20.domain.stats.Stats

abstract class Ancestry(private val stats: Stats) : dev.patbeagan.b20.domain.Effect, Stats by stats {
    override var roundsLeft: Int = Int.MAX_VALUE
    override var type: dev.patbeagan.b20.domain.Effect.Type = dev.patbeagan.b20.domain.Effect.Type.ANCESTRY
    override var name: String = "${this::class.java.simpleName} ancestry"
    override var descriptionActivation: String = ""
    override var descriptionDeactivation: String = ""

    abstract fun decide(mob: Mob, behavior: MobBehavior, worldState: WorldState): Action
}
