package dev.patbeagan.base

import kotlin.random.Random

object Probability {
    private const val seed = 123
    private val random = Random(seed)
    fun probability(
        p: Double,
        actionSucceed: () -> Unit,
        actionFail: () -> Unit
    ) = if (random.nextDouble() < p) actionSucceed() else actionFail()

    fun probability(
        p: Double,
        actionSucceed: () -> Unit
    ) = probability(p, actionSucceed, {})
}