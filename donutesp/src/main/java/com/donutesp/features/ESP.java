package com.donutesp.features;

import com.donutesp.DonutESP;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;
import java.util.List;

public class ESP {

    public boolean enabled     = true;
    public boolean players     = true;
    public boolean mobs        = false;
    public boolean chests      = true;
    public boolean tracerLines = true;

    public int colorPlayer = 0xFF00FFFF;
    public int colorMob    = 0xFFFF4040;
    public int colorChest  = 0xFFFFD700;

    public List<Entity> getTargets(MinecraftClient mc) {
        List<Entity> out = new ArrayList<>();
        if (!enabled || mc.world == null) return out;

        for (Entity e : mc.world.getEntities()) {
            if (e == mc.player) continue;
            if (e == DonutESP.freeCam.getPhantom()) continue;
            if (players && e instanceof PlayerEntity)  out.add(e);
            if (mobs    && e instanceof HostileEntity) out.add(e);
        }
        return out;
    }

    public void toggle() { enabled = !enabled; }
}
