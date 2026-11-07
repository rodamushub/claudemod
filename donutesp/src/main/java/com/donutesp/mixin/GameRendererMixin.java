package com.donutesp.mixin;

import com.donutesp.DonutESP;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Inject(method = "getFocusedEntity", at = @At("RETURN"), cancellable = true)
    private void redirectCamera(CallbackInfoReturnable<Entity> cir) {
        if (DonutESP.freeCam.enabled && DonutESP.freeCam.getPhantom() != null)
            cir.setReturnValue(DonutESP.freeCam.getPhantom());
    }
}
