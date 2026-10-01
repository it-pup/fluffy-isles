package pet.itpuppy.fluffyisles.features

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.world.item.ItemStack
import pet.itpuppy.fluffyisles.FluffyIsles
import pet.itpuppy.fluffyisles.api.ItemRarity

object RarityHighlights {
    val sprite = FluffyIsles.id("hud_item_rarity_highlight")

    fun renderHighlight(stack: ItemStack, x: Int, y: Int, graphics: GuiGraphics) {
        if (!FluffyIsles.config.rarityHighlightSection.isEnabled) return
        val rarity = ItemRarity.fromItemStack(stack) ?: return

        graphics.blitSprite(
            RenderPipelines.GUI_TEXTURED,
            sprite, x, y,
            16, 16,
            rarity.color
        )
    }
}