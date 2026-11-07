package com.donutesp.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

public final class Renderer3D {

    private Renderer3D() {}

    public static void drawBox(Matrix4f modelView, Box box, int argb, float lineWidth) {
        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.lineWidth(lineWidth);

        float a  = ((argb >> 24) & 0xFF) / 255f;
        float r  = ((argb >> 16) & 0xFF) / 255f;
        float g  = ((argb >>  8) & 0xFF) / 255f;
        float b  = ((argb      ) & 0xFF) / 255f;

        Tessellator tes = Tessellator.getInstance();
        BufferBuilder buf = tes.getBuffer();
        RenderSystem.setShader(GameRenderer::getRenderTypeLinesProgram);

        MatrixStack ms = new MatrixStack();
        buf.begin(VertexFormat.DrawMode.LINES, VertexFormats.LINES);
        WorldRenderer.drawBox(ms, buf,
                box.minX, box.minY, box.minZ,
                box.maxX, box.maxY, box.maxZ,
                r, g, b, a);
        tes.draw();

        RenderSystem.enableDepthTest();
    }

    public static void drawLine(Matrix4f modelView, Vec3d from, Vec3d to, int argb, float lineWidth) {
        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.lineWidth(lineWidth);

        float a  = ((argb >> 24) & 0xFF) / 255f;
        float r  = ((argb >> 16) & 0xFF) / 255f;
        float g  = ((argb >>  8) & 0xFF) / 255f;
        float bl = ((argb      ) & 0xFF) / 255f;

        Tessellator tes = Tessellator.getInstance();
        BufferBuilder buf = tes.getBuffer();
        RenderSystem.setShader(GameRenderer::getRenderTypeLinesProgram);

        buf.begin(VertexFormat.DrawMode.LINES, VertexFormats.LINES);
        buf.vertex(modelView, (float) from.x, (float) from.y, (float) from.z)
           .color(r, g, bl, a).normal(0, 1, 0).next();
        buf.vertex(modelView, (float) to.x, (float) to.y, (float) to.z)
           .color(r, g, bl, a).normal(0, 1, 0).next();
        tes.draw();

        RenderSystem.enableDepthTest();
    }

    public static void drawEntityBox(Matrix4f modelView, Entity e, int argb, float delta) {
        Vec3d cam = MinecraftClient.getInstance().gameRenderer.getCamera().getPos();
        Vec3d pos = e.getLerpedPos(delta).subtract(cam);
        Box box = new Box(
                pos.x - e.getWidth() / 2.0, pos.y, pos.z - e.getWidth() / 2.0,
                pos.x + e.getWidth() / 2.0, pos.y + e.getHeight(), pos.z + e.getWidth() / 2.0);
        drawBox(modelView, box, argb, 1.5f);
    }
}
