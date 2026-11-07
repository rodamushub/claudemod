package com.donutesp.mixin;

import com.donutesp.ModHider;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.stream.Collectors;

@Mixin(value = FabricLoader.class, remap = false)
public interface FabricLoaderMixin {

    @Inject(method = "getAllMods", at = @At("RETURN"), cancellable = true, remap = false)
    private void filterMods(CallbackInfoReturnable<Collection<ModContainer>> cir) {
        Collection<ModContainer> filtered = cir.getReturnValue().stream()
                .filter(mc -> !ModHider.HIDDEN_IDS.contains(mc.getMetadata().getId()))
                .collect(Collectors.toList());
        cir.setReturnValue(filtered);
    }
}
