package pet.itpuppy.fluffyisles.api

import net.minecraft.core.component.DataComponents
import net.minecraft.world.item.ItemStack
import pet.itpuppy.fluffyisles.utils.ColorUtils

object ItemRarity {
    enum class Rarities(
        val displayName: String,
        val color: Int,
    ) {
        COMMON(
            "Common",
            ColorUtils.colorOf(236, 190, 116),
        ),
        UNCOMMON(
            "Uncommon",
            ColorUtils.colorOf(5, 157, 28),
        ),
        RARE(
            "Rare",
            ColorUtils.colorOf(31, 140, 217),
        ),
        EPIC(
            "Epic",
            ColorUtils.colorOf(130, 42, 149),
        ),
        LEGENDARY(
            "Legendary",
            ColorUtils.colorOf(255, 119, 41),
        )
    }

    fun fromItemStack(stack: ItemStack): Rarities? {
        if (stack.isEmpty) return null

        val lore = stack.get(DataComponents.LORE) ?: return null
        if (lore.lines.size < 2) return null

        return Rarities.entries.find {
            lore.lines[1].string.contains(it.displayName)
        }
    }
}