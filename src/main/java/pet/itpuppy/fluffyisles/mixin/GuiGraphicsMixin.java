package pet.itpuppy.fluffyisles.mixin;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pet.itpuppy.fluffyisles.FluffyIsles;
import pet.itpuppy.fluffyisles.features.RarityHighlights;

import java.util.List;

@Mixin(GuiGraphics.class)
public class GuiGraphicsMixin {
    @Inject(
            method = "renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;III)V",
            at = @At("HEAD")
    )
    private void onRenderItem(
            LivingEntity entity,
            Level level,
            ItemStack stack,
            int x,
            int y,
            int seed,
            CallbackInfo ci
    ) {
        RarityHighlights.INSTANCE.renderHighlight(stack, x, y, (GuiGraphics) (Object) this);
    }

    @Inject(
            method = "setTooltipForNextFrameInternal",
            at = @At("HEAD"),
            cancellable = true
    )
    void onSetTooltipForNextFrameInternal(
            Font font,
            List<ClientTooltipComponent> components,
            int x,
            int y,
            ClientTooltipPositioner positioner,
            @Nullable Identifier background,
            boolean focused,
            CallbackInfo ci
    ) {
        if (!FluffyIsles.config.getMiscSection().getHideEmptyTooltips()) return;

        if (components.size() == 1 && components.getFirst().getWidth(FluffyIsles.INSTANCE.getClient().font) == 0) {
            ci.cancel();
        }
    }
}
