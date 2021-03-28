package com.pbeagan.data

import com.pbeagan.util.DiceRoll

interface Stats {

    // Combat
    val baseAtkMelee: AttackValue
    val baseAtkRanged: AttackValue
    val baseAtkThrow: AttackValue

    // Misc
    val awareness: MiscValue
    val spirit: MiscValue
    val speed: MiscValue

    // Mental
    val presence: MentalValue
    val cunning: MentalValue
    val persuasion: MentalValue
    val tenacity: MentalValue
    val fortitude: MentalValue

    // Physical
    val strength: PhysicalValue
    val agility: PhysicalValue
    val precision: PhysicalValue
    val endurance: PhysicalValue
    val durability: PhysicalValue

    // Total hearts
    val totalHearts: HealthValue

    val visionBright: VisionValue
    val visionDim: VisionValue
    val visionDark: VisionValue
    val visionNone: VisionValue
}


typealias IntContainer = ValueContainer<Int>

interface AddableInt<T : IntContainer> : ValueInt<T> {
    operator fun minus(a: T): T = create(value - a.value)
    operator fun plus(a: T): T = create(value + a.value)
}

interface AdjustableInt<T : IntContainer> : ValueInt<T> {
    fun adjustBy(a: Int) = create(value + a)
    fun adjustBy(a: DiceRoll) = create(value + a.value)
}

interface ValueInt<T : IntContainer> : IntContainer, IntContainerFactory<T>
interface IntContainerFactory<T : IntContainer> {
    /**
     * Default assumes usage as an inline class.
     * If this is not an inline class, please override.
     */
    @Suppress("UNCHECKED_CAST")
    fun create(a: Int): T = this::class.constructors.first().call(a) as T
}

interface ValueContainer<T> {
    val value: T
}

interface StatValueI<T : IntContainer> : MobValueInt<T> {
    fun mod(): T = create((value - 10) / 2)
}

interface MobValueInt<T : IntContainer> : ValueInt<T>,
    AdjustableInt<T>,
    AddableInt<T>

inline class AttackValue(override val value: Int) : MobValueInt<AttackValue>,
    Comparable<DefenseValue> {
    override fun compareTo(other: DefenseValue): Int = value - other.value
}

inline class DefenseValue(override val value: Int) : MobValueInt<DefenseValue>,
    Comparable<AttackValue> {
    override fun compareTo(other: AttackValue): Int = other.value - value
}

inline class VisionValue(override val value: Int) : MobValueInt<VisionValue>
inline class PhysicalValue(override val value: Int) : StatValueI<PhysicalValue>
inline class MentalValue(override val value: Int) : StatValueI<MentalValue>
inline class MiscValue(override val value: Int) : StatValueI<MiscValue>
inline class HealthValue(override val value: Int) : MobValueInt<HealthValue> {
    operator fun plus(a: PhysicalValue): HealthValue = HealthValue(value + a.value)
}
