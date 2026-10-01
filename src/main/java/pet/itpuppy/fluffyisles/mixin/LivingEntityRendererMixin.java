package pet.itpuppy.fluffyisles.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import pet.itpuppy.fluffyisles.FluffyIsles;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {
    @ModifyReturnValue(method = "shouldShowName(Lnet/minecraft/world/entity/LivingEntity;D)Z", at = @At("RETURN"))
    public boolean onShouldShowName(boolean original, @Local(argsOnly = true) LivingEntity livingEntity) {
        if (livingEntity == Minecraft.getInstance().player && FluffyIsles.config.getMiscSection().getShowOwnNametag()) {
            return true;
        }

        return original;
    }
}