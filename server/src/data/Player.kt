package data

import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.round

data class Player(
    val name: String,
    val attr: Attributes,
    val items: List<Item>,
    val location: Location
) {
    val wieldedItems
        get() = items.filterIsInstance(Wieldable::class.java).filter { it.isWielded }
    val hearts
        get() = roundUpToInt { ceil(attr.endurance.mod()) + 3 }
    private val encumbrance
        get() = items.sumBy { it.encumbrance }

    val dodge
        get() = roundUpToInt { 5 + attr.agility.mod() - encumbrance }
    val armor
        get() = wieldedItems.sumBy { it.armor }
    val speed
        get() = roundUpToInt {
            val round = round(4 + attr.agility.mod() / 2)
            val max = max(0.0, attr.endurance.mod() - encumbrance)
            round - max
        }

    val baseAtkMelee: Int
        get() = roundUpToInt { attr.strength.mod() + 1 }
    val baseAtkThrow: Int
        get() = roundUpToInt {
            val listOf = listOf(attr.strength, attr.strength, attr.precision)
            listOf.map { it.mod() }.average()
        }
    val baseAtkRanged
        get() = roundUpToInt {
            val listOf = listOf(attr.strength, attr.precision, attr.precision)
            listOf.map { it.mod() }.average()
        }

    private fun Int.mod(): Double = ((this - 10) / 2).toDouble()
    private inline fun roundUpToInt(action: () -> Double): Int = ceil(action()).toInt()


    companion object {
        fun random(): Player = Player(
            listOf("Ken", "Ryu", "Cloud", "John").random(),
            Attributes.random(),
            listOf(object : Item {
                override val encumbrance = 0
            }),
            Location.random()
        )

    }

    fun show(): String = this.toString()
        .replace(",", ",\n")
        .let { dataClassOutput ->
            val form = { s: String, i: Int -> "\n$s: $i" }
            val self = this

            buildString {
                append(dataClassOutput)
                append(form("armor", self.armor))
                append(form("dodge", self.dodge))
                append(form("baseAtkMelee", self.baseAtkMelee))
                append(form("hearts", self.hearts))
                append(form("speed", self.speed))
            }
        }
        .also { println(it) }
}