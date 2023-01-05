package com.pbeagan.stats

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
     * Default assumes usage as an data class.
     * If this is not an data class, please override.
     */
    @Suppress("UNCHECKED_CAST")
    fun create(a: Int): T
//    = this::class.constructors.first{it.parameters.size == 1 && it.parameters . all { it }}.call(a) as T
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

data class AttackValue(val v: Int) : MobValueInt<AttackValue>,
    Comparable<DefenseValue> {
    override fun compareTo(other: DefenseValue): Int = value - other.value
    override fun create(a: Int): AttackValue = AttackValue(a)
    override val value: Int get() = v
}

data class DefenseValue(val v: Int) : MobValueInt<DefenseValue>,
    Comparable<AttackValue> {
    override fun compareTo(other: AttackValue): Int = other.value - value
    override fun create(a: Int): DefenseValue = DefenseValue(a)
    override val value: Int get() = v
}

data class VisionValue(val v: Int) : MobValueInt<VisionValue> {
    override fun create(a: Int): VisionValue = VisionValue(a)
    override val value: Int get() = v
}

data class PhysicalValue(val v: Int) : StatValueI<PhysicalValue> {
    override fun create(a: Int): PhysicalValue = PhysicalValue(a)
    override val value: Int get() = v
}

data class MentalValue(val v: Int) : StatValueI<MentalValue> {
    override fun create(a: Int): MentalValue = MentalValue(a)
    override val value: Int get() = v
}

data class MiscValue(val v: Int) : StatValueI<MiscValue> {
    override fun create(a: Int): MiscValue = MiscValue(a)
    override val value: Int get() = v
}

data class HealthValue(val v: Int) : MobValueInt<HealthValue> {
    operator fun plus(a: PhysicalValue): HealthValue = HealthValue(value + a.value)
    override fun create(a: Int): HealthValue = HealthValue(a)
    override val value: Int get() = v
}
