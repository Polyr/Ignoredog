package io.github.synchronousx.ignoredog.listeners;

import com.mojang.authlib.GameProfile;
import io.github.synchronousx.ignoredog.IgnoredogMod;
import io.github.synchronousx.ignoredog.utils.Logger;
import io.github.synchronousx.ignoredog.utils.player.PlayerId;
import io.github.synchronousx.ignoredog.utils.player.PlayerUtils;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Optional;

public class PlayerRenderListener {
    private final IgnoredogMod mod;

    public PlayerRenderListener(final IgnoredogMod mod) {
        this.mod = mod;
    }

    @SubscribeEvent
    public void onPrePlayerRender(final RenderPlayerEvent.Pre event) {
        if (this.mod.isEnabled() && event.entityPlayer instanceof EntityOtherPlayerMP) {
            final EntityOtherPlayerMP otherPlayer = (EntityOtherPlayerMP) event.entityPlayer;
            final GameProfile gameProfile = otherPlayer.getGameProfile();
            final PlayerId playerId = new PlayerId(gameProfile.getId(), gameProfile.getName());

            if (PlayerUtils.doesPlayerHaveBotSkin(otherPlayer) && !PlayerUtils.isPlayerVisibleOnTab(playerId) && !this.mod.getBotUtils().getPotentialBots().contains(playerId) && Optional.ofNullable(this.mod.getPlayerValidator().getPlayerCache().get(playerId)).orElse(PlayerUtils.AccountType.UNKNOWN) != PlayerUtils.AccountType.NONEXISTENT) {
                this.mod.getBotUtils().getPotentialBots().add(playerId);
                if (this.mod.sendDebugMessages()) {
                    Logger.log(Logger.translateAmpersandFormatting("&d " + playerId.getName().orElse("") + "&r has a bot skin and is not visible on Tab, so they have been &a&ladded&r as a potential bot."));
                }
            }

            if (this.mod.getBotUtils().getPotentialBots().contains(playerId)) {
                event.setCanceled(true);
            }
        }
    }
}
