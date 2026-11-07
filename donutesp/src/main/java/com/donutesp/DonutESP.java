package com.donutesp;

import com.donutesp.features.BaseFinder;
import com.donutesp.features.ESP;
import com.donutesp.features.FreeCam;
import com.donutesp.features.Tracers;
import com.donutesp.gui.ClickGUI;
import com.donutesp.render.HUD;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class DonutESP implements ClientModInitializer {

    public static final String MOD_ID = "donutesp";

    public static final ESP        esp        = new ESP();
    public static final BaseFinder baseFinder = new BaseFinder();
    public static final FreeCam    freeCam    = new FreeCam();
    public static final Tracers    tracers    = new Tracers();
    public static final ClickGUI   clickGUI   = new ClickGUI();

    private static KeyBinding kbGui;
    private static KeyBinding kbFreeCam;
    private static KeyBinding kbESP;
    private static KeyBinding kbBaseFinder;

    @Override
    public void onInitializeClient() {
        kbGui        = reg("Open GUI",    GLFW.GLFW_KEY_RIGHT_SHIFT);
        kbFreeCam    = reg("FreeCam",     GLFW.GLFW_KEY_F);
        kbESP        = reg("ESP",         GLFW.GLFW_KEY_G);
        kbBaseFinder = reg("BaseFinder",  GLFW.GLFW_KEY_B);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (kbGui.wasPressed())        clickGUI.toggle();
            if (kbFreeCam.wasPressed())    freeCam.toggle();
            if (kbESP.wasPressed())        esp.toggle();
            if (kbBaseFinder.wasPressed()) baseFinder.toggle();

            freeCam.tick(client);
            baseFinder.tick(client);
        });

        HudRenderCallback.EVENT.register((drawContext, tickDelta) ->
                HUD.render(drawContext, tickDelta));
    }

    private static KeyBinding reg(String name, int key) {
        return KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.donutesp." + name.toLowerCase().replace(' ', '_'),
                InputUtil.Type.KEYSYM, key,
                "category.donutesp.main"));
    }
}
