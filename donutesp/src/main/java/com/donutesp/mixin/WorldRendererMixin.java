package com.donutesp.mixin;

import com.donutesp.DonutESP;
import com.donutesp.render.Renderer3D;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

    @Inject(method = "render",
            at = @At(value = "INVOKE",
                     target = "Lnet/minecraft/client/render/WorldRenderer;renderWeather(Lnet/minecraft/client/render/LightmapTextureManager;FDD)V"))
    private void onRender(RenderTickCounter tickCounter,
                          boolean renderBlockOutline,
                          Camera camera,
                          GameRenderer gameRenderer,
                          LightmapTextureManager lightmapTextureManager,
                          Matrix4f projection,
                          Matrix4f modelView,
                          CallbackInfo ci) {

        MinecraftClient mc = MinecraftClient.getInstance();
        float delta = tickCounter.getTickDelta(true);

        if (DonutESP.esp.enabled) {
            for (Entity e : DonutESP.esp.getTargets(mc)) {
                int color = (e instanceof PlayerEntity)  ? DonutESP.esp.colorPlayer
                          : (e instanceof HostileEntity) ? DonutESP.esp.colorMob
                          : 0xFFFFFFFF;
                Renderer3D.drawEntityBox(modelView, e, color, delta);
            }
        }

        if (DonutESP.tracers.enabled) {
            DonutESP.tracers.render(mc, camera, projection, modelView);
        }
    }
}
