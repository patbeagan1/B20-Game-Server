package com.pbeagan.data

interface Stats {

    // Combat
    val baseAtkMelee: CombatValue
    val baseAtkRanged: CombatValue
    val baseAtkThrow: CombatValue

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

interface ValueInt<T : ValueContainer<Int>> : ValueContainer<Int> {
    @Suppress("UNCHECKED_CAST")
    fun create(a: Int): T = this::class.constructors.first().call(a) as T
    operator fun minus(a: T): T = create(value - a.value)
    operator fun plus(a: T): T = create(value + a.value)
}

interface ValueContainer<T> {
    val value: T
}

interface StatValueI<T : ValueContainer<Int>> : ValueInt<T> {
    fun mod(): T = create((value - 10) / 2)
}

inline class CombatValue(override val value: Int) : ValueInt<CombatValue> {
    override fun create(a: Int): CombatValue = CombatValue(a)
}

inline class VisionValue(override val value: Int) : ValueInt<VisionValue> {
    override fun create(a: Int): VisionValue = VisionValue(a)
}

inline class PhysicalValue(override val value: Int) : StatValueI<PhysicalValue>

inline class MentalValue(override val value: Int) : StatValueI<MentalValue> {
    override fun create(a: Int): MentalValue = MentalValue(a)
}

inline class MiscValue(override val value: Int) : StatValueI<MiscValue> {
    override fun create(a: Int): MiscValue = MiscValue(a)
}

inline class HealthValue(override val value: Int) : StatValueI<HealthValue> {
    override fun create(a: Int): HealthValue = HealthValue(a)
    operator fun plus(a: PhysicalValue): HealthValue = HealthValue(value + a.value)
}
