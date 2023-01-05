package com.pbeagan.domain


import com.pbeagan.consolevision.Coord
import com.pbeagan.util.FlagSet
import com.pbeagan.util.startsWith
import com.pbeagan.consolevision.TerminalColorStyle.Colors.Cyan
import com.pbeagan.consolevision.TerminalColorStyle.style
import com.pbeagan.domain.flags.AffectedByMagic
import com.pbeagan.domain.flags.ItemFlags
import com.pbeagan.domain.flags.VisibleBy

data class ItemData(
    val id: Int,
    val names: List<String>,
    val descriptionOnExamination: String,
    val descriptionInRoom: String,
    override var locationInRoom: Coord
) : HasLocation {
    val nameStyled: String = names.first().style(colorForeground = Cyan)
    val affectedByMagicPossible: FlagSet<AffectedByMagic> = AffectedByMagic.defaultItem
    val affectedByMagicCurrent: FlagSet<AffectedByMagic> = FlagSet.of()
    val containsInnerItem: ItemData? = null
    val itemFlags: FlagSet<ItemFlags> = ItemFlags.default
    val visibleBy: FlagSet<VisibleBy> = VisibleBy.defaultItem
    val flagHandlers = mutableMapOf<ItemFlags, FlagHandler?>()

    fun setItemFlags(flags: ItemFlags, handler: FlagHandler? = null) {
        itemFlags.add(flags)
        if (handler != null) {
            flagHandlers[flags] = handler
        }
    }

    fun nameStartsWith(item: String) =
        names.any { it.startsWith(item) }

    interface FlagHandler {
        fun invoke(self: Mob) = Unit
        val descriptionOnActivation: String? get() = null
        val descriptionOnDuration: String? get() = null
        val descriptionOnCompletion: String? get() = null
    }
}