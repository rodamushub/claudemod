package com.donutesp.render;

import com.donutesp.DonutESP;
import com.donutesp.features.BaseFinder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public final class HUD {

    private HUD() {}

    public static void render(DrawContext ctx, float delta) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.options.hudHidden || mc.player == null) return;
        TextRenderer tr = mc.textRenderer;
        int sw = mc.getWindow().getScaledWidth();

        // Active modules — top left
        List<String> active = new ArrayList<>();
        if (DonutESP.esp.enabled)        active.add("§bESP");
        if (DonutESP.freeCam.enabled)    active.add("§aFreeCam");
        if (DonutESP.baseFinder.enabled) active.add("§eBaseFinder");
        if (DonutESP.tracers.enabled)    active.add("§dTracers");

        int y = 4;
        for (String mod : active) {
            ctx.fill(2, y - 1, 2 + tr.getWidth(mod) + 4, y + 9, 0x88000000);
            ctx.drawText(tr, Text.literal(mod), 4, y, 0xFFFFFFFF, false);
            y += 11;
        }

        // BaseFinder results — top right
        if (DonutESP.baseFinder.enabled) {
            List<BaseFinder.BaseCluster> clusters = DonutESP.baseFinder.getClusters();
            if (!clusters.isEmpty()) {
                String header = "§e§lBASES DETECTED";
                int hx = sw - tr.getWidth(header) - 6;
                ctx.fill(hx - 2, 2, sw - 2, 4 + (Math.min(clusters.size(), 10) + 1) * 11, 0x99000000);
                ctx.drawText(tr, Text.literal(header), hx, 4, 0xFFFFFFFF, false);

                BlockPos player = mc.player.getBlockPos();
                int ry = 15;
                for (int i = 0; i < Math.min(clusters.size(), 10); i++) {
                    BaseFinder.BaseCluster c = clusters.get(i);
                    int dist = (int) Math.sqrt(player.getSquaredDistance(c.centroid()));
                    String line = String.format("§7#%d §f%d,%d,%d §8(%dm) §ascore:%d",
                            i + 1,
                            c.centroid().getX(), c.centroid().getY(), c.centroid().getZ(),
                            dist, c.score());
                    int lx = sw - tr.getWidth(line) - 6;
                    ctx.drawText(tr, Text.literal(line), lx, ry, 0xFFFFFFFF, false);
                    ry += 11;
                }
            }
        }
    }
}
