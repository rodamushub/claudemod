package com.donutesp.features;

import com.donutesp.DonutESP;
import com.donutesp.render.Renderer3D;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

public class Tracers {

    public boolean enabled = true;

    public void render(MinecraftClient mc, Camera camera, Matrix4f projection, Matrix4f modelView) {
        if (!enabled || mc.world == null) return;

        for (Entity e : DonutESP.esp.getTargets(mc)) {
            int color = (e instanceof PlayerEntity)  ? DonutESP.esp.colorPlayer
                      : (e instanceof HostileEntity) ? DonutESP.esp.colorMob
                      : 0xFFFFFFFF;

            Vec3d foot = e.getLerpedPos(mc.getTickDelta()).subtract(camera.getPos());
            Renderer3D.drawLine(modelView,
                    new Vec3d(0, 0, 0),
                    foot.add(0, e.getHeight() / 2.0, 0),
                    color, 1.0f);
        }
    }

    public void toggle() { enabled = !enabled; }
}
