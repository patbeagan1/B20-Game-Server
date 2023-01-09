package dev.patbeagan.b20.contextual

import dev.patbeagan.b20.util.commonPrefixWithIgnoreCase
import dev.patbeagan.b20.domain.HasLocation
import dev.patbeagan.b20.domain.flags.AffectedByMagic
import dev.patbeagan.b20.domain.flags.ItemFlags
import dev.patbeagan.b20.domain.flags.VisibleBy
import dev.patbeagan.base.FlagSet
import dev.patbeagan.consolevision.ansi.AnsiColor
import dev.patbeagan.consolevision.ansi.style
import dev.patbeagan.consolevision.types.CompressedPoint

data class ItemData(
    val id: Int,
    val names: List<String>,
    val descriptionOnExamination: String,
    val descriptionInRoom: String,
    override var locationInRoom: CompressedPoint,
) : HasLocation {
    val nameStyled: String = names.first().style(colorForeground = AnsiColor.Cyan)
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
        names.any { it.commonPrefixWithIgnoreCase(item) }

    interface FlagHandler {
        fun invoke(self: Mob) = Unit
        val descriptionOnActivation: String? get() = null
        val descriptionOnDuration: String? get() = null
        val descriptionOnCompletion: String? get() = null
    }
}