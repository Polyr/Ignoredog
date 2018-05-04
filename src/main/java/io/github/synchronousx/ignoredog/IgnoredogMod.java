package io.github.synchronousx.ignoredog;

import io.github.synchronousx.ignoredog.listeners.ClientTickListener;
import io.github.synchronousx.ignoredog.listeners.KeyInputListener;
import io.github.synchronousx.ignoredog.listeners.PlayerRenderListener;
import io.github.synchronousx.ignoredog.utils.Key;
import io.github.synchronousx.ignoredog.utils.player.BotUtils;
import io.github.synchronousx.ignoredog.utils.player.PlayerValidator;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.Arrays;

@Mod(modid = IgnoredogMod.MODID, useMetadata = true, clientSideOnly = true)
public class IgnoredogMod {
    public static final String MODID = "ignoredog";
    private final Object[] listeners = {
            new ClientTickListener(this),
            new KeyInputListener(this),
            new PlayerRenderListener(this)
    };

    private final BotUtils botUtils = new BotUtils(this);
    private final PlayerValidator playerValidator = new PlayerValidator(this);
    private boolean enabled = true;
    private boolean debugMessages = false;

    @EventHandler
    public void preInit(final FMLPreInitializationEvent event) {
        Key.registerKeyBindings();
    }

    @EventHandler
    public void init(final FMLInitializationEvent event) {
        Arrays.stream(this.listeners).forEach(MinecraftForge.EVENT_BUS::register);
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

    public boolean sendDebugMessages() {
        return this.debugMessages;
    }

    public void setDebugMessages(final boolean debugMessages) {
        this.debugMessages = debugMessages;
    }

    public BotUtils getBotUtils() {
        return this.botUtils;
    }

    public PlayerValidator getPlayerValidator() {
        return this.playerValidator;
    }
}
