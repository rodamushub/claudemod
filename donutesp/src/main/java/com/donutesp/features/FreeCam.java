package com.donutesp.features;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

public class FreeCam {

    public  boolean enabled  = false;
    private Entity  phantom  = null;
    private Vec3d   savedPos = null;
    private float   savedYaw, savedPitch;
    private static final float SPEED = 0.5f;

    public void toggle() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.world == null) return;

        enabled = !enabled;
        if (enabled) {
            savedPos   = mc.player.getPos();
            savedYaw   = mc.player.getYaw();
            savedPitch = mc.player.getPitch();

            phantom = new LivingEntity(EntityType.PIG, mc.world) {};
            phantom.setPos(savedPos.x, savedPos.y + mc.player.getStandingEyeHeight(), savedPos.z);
            phantom.setYaw(savedYaw);
            phantom.setPitch(savedPitch);

            mc.setCameraEntity(phantom);
        } else {
            mc.setCameraEntity(mc.player);
            phantom = null;
        }
    }

    public void tick(MinecraftClient mc) {
        if (!enabled || phantom == null || mc.player == null) return;

        double yawRad   = Math.toRadians(phantom.getYaw());
        double pitchRad = Math.toRadians(phantom.getPitch());

        double dx = 0, dy = 0, dz = 0;
        long win = mc.getWindow().getHandle();

        if (GLFW.glfwGetKey(win, GLFW.GLFW_KEY_W) == GLFW.GLFW_PRESS) {
            dx -= Math.sin(yawRad) * Math.cos(pitchRad) * SPEED;
            dy -= Math.sin(pitchRad) * SPEED;
            dz += Math.cos(yawRad) * Math.cos(pitchRad) * SPEED;
        }
        if (GLFW.glfwGetKey(win, GLFW.GLFW_KEY_S) == GLFW.GLFW_PRESS) {
            dx += Math.sin(yawRad) * Math.cos(pitchRad) * SPEED;
            dy += Math.sin(pitchRad) * SPEED;
            dz -= Math.cos(yawRad) * Math.cos(pitchRad) * SPEED;
        }
        if (GLFW.glfwGetKey(win, GLFW.GLFW_KEY_A) == GLFW.GLFW_PRESS) {
            dx -= Math.cos(yawRad) * SPEED;
            dz -= Math.sin(yawRad) * SPEED;
        }
        if (GLFW.glfwGetKey(win, GLFW.GLFW_KEY_D) == GLFW.GLFW_PRESS) {
            dx += Math.cos(yawRad) * SPEED;
            dz += Math.sin(yawRad) * SPEED;
        }
        if (GLFW.glfwGetKey(win, GLFW.GLFW_KEY_SPACE) == GLFW.GLFW_PRESS)      dy += SPEED;
        if (GLFW.glfwGetKey(win, GLFW.GLFW_KEY_LEFT_SHIFT) == GLFW.GLFW_PRESS) dy -= SPEED;

        Vec3d pos = phantom.getPos();
        phantom.setPos(pos.x + dx, pos.y + dy, pos.z + dz);
        phantom.setYaw(mc.player.getYaw());
        phantom.setPitch(mc.player.getPitch());
    }

    public Entity getPhantom() { return phantom; }
}
