package com.donutesp.gui;

import com.donutesp.DonutESP;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ClickGUI extends Screen {

    private static final int PANEL_W = 160;
    private static final int PANEL_H = 240;
    private static final int HEADER  = 20;
    private static final int ROW_H   = 24;
    private static final int ACCENT  = 0xFF00BFFF;
    private static final int BG      = 0xCC101820;
    private static final int FG      = 0xFFEEEEEE;
    private static final int HOVER   = 0x33FFFFFF;
    private static final int ON      = 0xFF00FF88;
    private static final int OFF     = 0xFF666666;

    private float slideY  = -PANEL_H;
    private float targetY = 0;
    private boolean open  = false;

    public ClickGUI() {
        super(Text.literal("DonutESP"));
    }

    public void toggle() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (!open) {
            open = true;
            mc.setScreen(this);
        } else {
            open = false;
            mc.setScreen(null);
        }
    }

    @Override
    protected void init() {
        targetY = (height - PANEL_H) / 2f;
        slideY  = -PANEL_H;
    }

    @Override
    public void render(DrawContext ctx, int mx, int my, float delta) {
        slideY += (targetY - slideY) * Math.min(delta * 0.4f, 1.0f);

        int px = (width  - PANEL_W) / 2;
        int py = (int) slideY;

        ctx.fill(px, py, px + PANEL_W, py + PANEL_H, BG);
        ctx.fill(px, py, px + PANEL_W, py + HEADER,  ACCENT);
        ctx.drawCenteredTextWithShadow(textRenderer,
                Text.literal("§l DonutESP ✦"),
                px + PANEL_W / 2, py + 5, FG);

        String[][] modules = {
            {"ESP",         DonutESP.esp.enabled        ? "ON" : "OFF"},
            {"FreeCam",     DonutESP.freeCam.enabled     ? "ON" : "OFF"},
            {"BaseFinder",  DonutESP.baseFinder.enabled  ? "ON" : "OFF"},
            {"Tracers",     DonutESP.tracers.enabled     ? "ON" : "OFF"},
        };

        for (int i = 0; i < modules.length; i++) {
            int ry  = py + HEADER + i * ROW_H;
            boolean hov = mx >= px && mx <= px + PANEL_W && my >= ry && my <= ry + ROW_H;
            if (hov) ctx.fill(px, ry, px + PANEL_W, ry + ROW_H, HOVER);
            ctx.fill(px + 8, ry + ROW_H - 1, px + PANEL_W - 8, ry + ROW_H, 0x22FFFFFF);

            String status      = modules[i][1];
            int    statusColor = status.equals("ON") ? ON : OFF;

            ctx.drawTextWithShadow(textRenderer, Text.literal(modules[i][0]),
                    px + 10, ry + 7, FG);
            ctx.drawTextWithShadow(textRenderer, Text.literal(status),
                    px + PANEL_W - textRenderer.getWidth(status) - 10, ry + 7, statusColor);
        }

        ctx.drawCenteredTextWithShadow(textRenderer,
                Text.literal("§8Click to toggle  •  RShift close"),
                px + PANEL_W / 2, py + PANEL_H - 12, 0xFF555555);
    }

    @Override
    public boolean mouseClicked(double mx, double my, int btn) {
        int px  = (width  - PANEL_W) / 2;
        int py  = (int) slideY;
        int row = (int)((my - py - HEADER) / ROW_H);

        if (mx >= px && mx <= px + PANEL_W && row >= 0 && row < 4) {
            switch (row) {
                case 0 -> DonutESP.esp.toggle();
                case 1 -> DonutESP.freeCam.toggle();
                case 2 -> DonutESP.baseFinder.toggle();
                case 3 -> DonutESP.tracers.toggle();
            }
            return true;
        }
        return super.mouseClicked(mx, my, btn);
    }

    @Override public boolean shouldPause()       { return false; }
    @Override public boolean shouldCloseOnEsc()  { return true;  }
    @Override public void    close()             { open = false; super.close(); }
}
