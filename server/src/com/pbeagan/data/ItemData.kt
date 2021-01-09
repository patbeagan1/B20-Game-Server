package com.pbeagan.data

import com.pbeagan.util.Coord
import com.pbeagan.util.FlagCombined
import com.pbeagan.util.createFlagSet
import com.pbeagan.util.startsWith
import com.pbeagan.writer.TerminalColorStyle.Colors.Cyan
import com.pbeagan.writer.TerminalColorStyle.style

data class ItemData(
    val id: Int,
    val names: List<String>,
    val descriptionOnExamination: String,
    val descriptionInRoom: String,
    override var locationInRoom: Coord
) : HasLocation {
    val nameStyled: String = names.first().style(colorForeground = Cyan)
    val affectedByMagicPossible: FlagCombined<AffectedByMagic> = AffectedByMagic.defaultItem
    val affectedByMagicCurrent: FlagCombined<AffectedByMagic> = createFlagSet()
    val containsInnerItem: ItemData? = null
    val itemFlags: FlagCombined<ItemFlags> = ItemFlags.default
    val visibleBy: FlagCombined<VisibleBy> = VisibleBy.defaultItem
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