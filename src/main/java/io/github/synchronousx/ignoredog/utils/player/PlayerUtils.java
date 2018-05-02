package io.github.synchronousx.ignoredog.utils.player;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetHandlerPlayClient;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

public class PlayerUtils {
    private PlayerUtils() {
    }

    public static UUID getUuidFromString(final String uuidString) {
        try {
            return UUID.fromString(uuidString);
        } catch (final IllegalArgumentException exception) {
            return UUID.fromString(uuidString.replaceFirst("(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5"));
        }
    }

    public static boolean doesPlayerHaveBotSkin(final AbstractClientPlayer player) {
        return Arrays.stream(BotUtils.SKINS).anyMatch(botSkin -> botSkin.equals(player.getLocationSkin().toString()));
    }

    public static boolean isPlayerVisibleOnTab(final PlayerId playerId) {
        final NetHandlerPlayClient netHandler = Minecraft.getMinecraft().getNetHandler();
        if (Optional.ofNullable(netHandler).isPresent()) {
            return netHandler.getPlayerInfoMap().stream().anyMatch(networkPlayerInfo -> Optional.of(networkPlayerInfo.getGameProfile().getName()).equals(playerId.getName()));
        }

        return false;
    }

    public enum AccountType {
        UNKNOWN, REQUESTED, EXISTENT, NONEXISTENT
    }
}
