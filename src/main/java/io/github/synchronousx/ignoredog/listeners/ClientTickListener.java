package io.github.synchronousx.ignoredog.listeners;

import io.github.synchronousx.ignoredog.IgnoredogMod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class ClientTickListener {
    private final IgnoredogMod mod;

    public ClientTickListener(final IgnoredogMod mod) {
        this.mod = mod;
    }

    @SubscribeEvent
    public void onClientTick(final ClientTickEvent event) {
        if (this.mod.isEnabled()) {
            this.mod.getPlayerValidator().cachePlayerIds(this.mod.getBotUtils().getBots());
            this.mod.getBotUtils().cleanBots();
        }
    }
}
