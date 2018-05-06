package io.github.synchronousx.ignoredog.utils.player;

import io.github.synchronousx.ignoredog.IgnoredogMod;
import io.github.synchronousx.ignoredog.utils.Logger;
import io.github.synchronousx.ignoredog.utils.player.PlayerUtils.AccountType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class BotUtils {
    public static final String[] SKINS = new String[] {
            "minecraft:skins/b2f3fc4516774596bfe5c2e16f661106f115ebf651e1287a7cca43399e8c194",
            "minecraft:textures/entity/steve.png",
            "minecraft:textures/entity/alex.png"
    };

    private final Set<PlayerId> potentialBots = ConcurrentHashMap.newKeySet();
    private final IgnoredogMod mod;

    public BotUtils(final IgnoredogMod mod) {
        this.mod = mod;
    }

    public void cleanBots() {
        this.potentialBots.stream().filter(playerId -> Optional.ofNullable(this.mod.getPlayerValidator().getPlayerCache().get(playerId)).orElse(AccountType.UNKNOWN) == AccountType.NONEXISTENT).forEach(playerId -> {
            this.potentialBots.remove(playerId);
            if (this.mod.sendDebugMessages()) {
                Logger.log(Logger.translateAmpersandFormatting("&d" + playerId.getName().orElse("") + "&r is not a premium account, so they have been &c&lremoved&r as a potential bot."));
            }
        });

        this.potentialBots.stream().filter(PlayerUtils::isPlayerVisibleOnTab).forEach(playerId -> {
            this.potentialBots.remove(playerId);
            if (this.mod.sendDebugMessages()) {
                Logger.log(Logger.translateAmpersandFormatting("&d" + playerId.getName().orElse("") + "&r is visible on Tab, so they have been &c&lremoved&r as a potential bot."));
            }
        });

        this.potentialBots.stream().filter(playerId -> {
            final WorldClient world = Minecraft.getMinecraft().theWorld;
            if (Optional.ofNullable(world).isPresent()) {
                final EntityPlayer player = world.getPlayerEntityByName(playerId.getName().orElse(""));
                return player instanceof AbstractClientPlayer && !PlayerUtils.doesPlayerHaveBotSkin((AbstractClientPlayer) player);
            }

            return false;
        }).forEach(playerId -> {
            this.potentialBots.remove(playerId);
            if (this.mod.sendDebugMessages()) {
                Logger.log(Logger.translateAmpersandFormatting("&d " + playerId.getName().orElse("") + "&r does not have a bot skin, so they have been &c&lremoved&r as a potential bot."));
            }
        });
    }

    public Set<PlayerId> getPotentialBots() {
        return this.potentialBots;
    }
}
