package pet.itpuppy.fluffyisles.api

import net.minecraft.client.gui.screens.Screen
import net.minecraft.world.item.ItemStack
import pet.itpuppy.fluffyisles.FluffyIsles
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

    val regex = """
        (Common|Uncommon|Rare|Epic|Legendary)\s+
        (?:
        (?:Raw\s+)?Material|Potion|Elixir|Ammo|
        
        Staff|Wand|Focus|Gauntlet|Tome|
        Sword|Axe|Spear|Dagger|Mace|
        Bow|Greatbow|Pot|Kunai|Crossbow|Handcannon|
        
        Helmet|Chestplate|Leggings|Boots|
        Quiver|Backpack|Pouch|Ring|Greave|Amulet|Glove|Gloves|
        
        Dish|Snack|Pet\sEgg|Pet|Consumable|Deployable|
        Utility|Weapon\sAugment|Quest\sItem|Item\sUtility|Upgrade\sStone|Crafting\sCatalyst|
        Hatchet|Pickaxe|Hoe|Fishing\sRod
        )
        (?:\s+Skin)?
        $
    """.trimIndent().toRegex(RegexOption.COMMENTS)

    fun fromItemStack(stack: ItemStack): Rarities? {
        if (stack.isEmpty) return null

        val lines = Screen.getTooltipFromItem(FluffyIsles.client, stack)
        for (line in lines) {
            if (line.string.trimStart() == "Class Core") return Rarities.LEGENDARY // couldn't be bothered any less to add complicated checks for this

            val match = regex.find(line.string)
            match?.let {
                return Rarities.entries.find { it.displayName == match.groupValues[1] }
            }
        }

        return null
    }
}