package pet.itpuppy.fluffyisles.mixin;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pet.itpuppy.fluffyisles.features.RarityHighlights;

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
}
