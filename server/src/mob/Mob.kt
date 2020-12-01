package mob

import actions.Action
import actions.AttackMelee
import actions.Look
import actions.Pass
import com.pbeagan.models.FlagCombined
import com.pbeagan.models.ItemData
import com.pbeagan.models.MobBehavior
import com.pbeagan.models.VisibleBy
import com.pbeagan.models.worldstate.Attr
import rooms
import writer.Writer

data class Mob constructor(
    val name: String,
    val descriptionProvider: DescriptionProvider = object :
        DescriptionProvider {
        override fun describe(behavior: MobBehavior, action: Action) =
            behavior.descriptionDefault
    },
    var action: Action = Pass,
    var behavior: MobBehavior,

    var location: Int = 0,
    var visited: MutableSet<Int> = mutableSetOf(0),
    var visibleBy: FlagCombined<VisibleBy> = VisibleBy.defaultMob,

    var attr: Attr = Attr(),
    var baseAtkMelee: Int = 0,
    var baseAtkRanged: Int = 0,
    var baseAtkThrow: Int = 0,

    var armor: Int = 0,
    var dodge: Int = 0,
    var hearts: Int = 0,

    var items: MutableList<ItemData> = mutableListOf()
) {

    val description get() = descriptionProvider.describe(behavior, action)

    interface DescriptionProvider {
        fun describe(behavior: MobBehavior, action: Action): String
    }
}

fun Mob.look(writer: Writer) = Look().also { it.writer = writer }(this)
fun Mob.currentRoom() = rooms[location]
fun Mob.currentRoomOtherMobs(list: List<Mob>) = list
    .filter { it.location == location && it != this }

fun Mob.attackFirstVisible(list: List<Mob>): Action? = list
    .firstOrNull { it.location == location && it != this }
    ?.let { AttackMelee(it) }