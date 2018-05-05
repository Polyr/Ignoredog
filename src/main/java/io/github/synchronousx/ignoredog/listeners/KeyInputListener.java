package io.github.synchronousx.ignoredog.listeners;

import io.github.synchronousx.ignoredog.IgnoredogMod;
import io.github.synchronousx.ignoredog.gui.IgnoredogGUI;
import io.github.synchronousx.ignoredog.utils.Key;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;

public class KeyInputListener {
    private final IgnoredogMod mod;

    public KeyInputListener(final IgnoredogMod mod) {
        this.mod = mod;
    }

    @SubscribeEvent
    public void onKeyInput(final KeyInputEvent event) {
        if (Key.OPEN_GUI.getKeyBinding().isPressed()) {
            Minecraft.getMinecraft().displayGuiScreen(new IgnoredogGUI(this.mod));
        }
    }
}
