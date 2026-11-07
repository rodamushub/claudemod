package com.donutesp.mixin;

import com.donutesp.DonutESP;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {

    @Inject(method = "tickMovement", at = @At("HEAD"))
    private void suppressMovement(CallbackInfo ci) {
        if (!DonutESP.freeCam.enabled) return;
        ClientPlayerEntity self = (ClientPlayerEntity)(Object)this;
        self.input.movementForward  = 0;
        self.input.movementSideways = 0;
        self.input.jumping          = false;
        self.input.sneaking         = false;
    }
}
