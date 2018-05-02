package io.github.synchronousx.ignoredog.listeners;

import io.github.synchronousx.ignoredog.IgnoredogMod;
import io.github.synchronousx.ignoredog.gui.IgnoredogGUI;
import io.github.synchronousx.ignoredog.utils.Key;
import io.github.synchronousx.ignoredog.utils.Logger;
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

        if (Key.LIST_CACHE.getKeyBinding().isPressed()) {
            Logger.log("Listing potential bots...");
            this.mod.getBotUtils().getBots().stream().map(playerId -> playerId.getName().orElse("")).forEach(name -> Logger.log(Logger.translateAmpersandFormatting("Name: &d" + name + "&r.")));
            Logger.log("Listing cached players...");
            this.mod.getPlayerValidator().getPlayerCache().forEach((playerId, accountType) -> Logger.log(Logger.translateAmpersandFormatting("Name: &d" + playerId.getName().orElse("") + "&r, account type: &d" + accountType.toString() + "&r.")));
        }
    }
}
