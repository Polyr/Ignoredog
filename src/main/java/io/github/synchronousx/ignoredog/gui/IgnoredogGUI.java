package io.github.synchronousx.ignoredog.gui;

import io.github.synchronousx.ignoredog.IgnoredogMod;
import io.github.synchronousx.ignoredog.gui.buttons.ActionButton;
import io.github.synchronousx.ignoredog.gui.buttons.ToggleButton;
import io.github.synchronousx.ignoredog.gui.buttons.ToggleButton.Status;
import io.github.synchronousx.ignoredog.utils.Logger;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.util.*;
import java.util.stream.Collectors;

public class IgnoredogGUI extends GuiScreen {
    public static final int WHITE = 16777215;

    private final IgnoredogMod mod;

    private int buttonWidth;
    private int buttonHeight;
    private int buttonYSeparation;

    private int scrollingTextXOffsetFromMiddle;
    private int scrollingTextWidth;
    private int scrollingTextHeight;
    private int scrollingTextYIncrement;
    private int scrollingTextAdditionalYMargin;
    private GuiScrollingTextList potentialBotsNameList;
    private GuiScrollingTextList playerCacheNameList;
    private int potentialBotsNameListSize = -1;
    private int playerCacheNameListSize = -1;

    public IgnoredogGUI(final IgnoredogMod mod) {
        this.mod = mod;
    }

    private Collection<GuiButton> getCenteredButtons(final Collection<GuiButton> buttons) {
        final int x = (this.width - this.buttonWidth) / 2;
        int y = (this.height - (buttons.size() * (this.buttonHeight + this.buttonYSeparation) - this.buttonYSeparation)) / 2;

        for (final GuiButton button : buttons) {
            button.xPosition = x;
            button.yPosition = y;
            y += this.buttonHeight + this.buttonYSeparation;
        }

        return buttons;
    }

    @Override
    public void initGui() {
        this.buttonWidth = this.width * 5 / 24;
        this.buttonHeight = this.height / 27;
        this.buttonYSeparation = this.height / 270;
        final List<GuiButton> buttons = new ArrayList<>();

        this.scrollingTextXOffsetFromMiddle = this.width / 8;
        this.scrollingTextWidth = this.width / 4;
        this.scrollingTextHeight = this.height / 4;
        this.scrollingTextYIncrement = this.height / 36;
        this.scrollingTextAdditionalYMargin = this.height / 54;

        this.updateScreen();

        buttons.add(new ToggleButton(0, 0, 0, this.buttonWidth, this.buttonHeight, status -> {
            switch (status) {
                case ENABLED:
                    this.mod.setEnabled(true);
                    if (this.mod.sendDebugMessages()) {
                        Logger.log(Logger.translateAmpersandFormatting("Toggled &a&lon&r."));
                    }

                    break;
                case DISABLED:
                    this.mod.setEnabled(false);
                    if (this.mod.sendDebugMessages()) {
                        Logger.log(Logger.translateAmpersandFormatting("Toggled &c&loff&r."));
                    }
            }
        }, "Ignoredog Mod", this.mod.isEnabled() ? Status.ENABLED : Status.DISABLED));

        buttons.add(new ToggleButton(1, 0, 0, this.buttonWidth, this.buttonHeight, status -> {
            switch (status) {
                case ENABLED:
                    this.mod.setDebugMessages(true);
                    break;
                case DISABLED:
                    this.mod.setDebugMessages(false);
            }
        }, "Debug Messages", this.mod.sendDebugMessages() ? Status.ENABLED : Status.DISABLED));

        this.buttonList.addAll(this.getCenteredButtons(buttons));
        this.buttonList.add(new ActionButton<Void>(2, (this.width / 2) - this.scrollingTextWidth - this.scrollingTextXOffsetFromMiddle, ((this.height + this.scrollingTextHeight) / 2) + this.buttonYSeparation, this.scrollingTextWidth, this.buttonHeight, "Clear Potential Bots", nothing -> this.mod.getBotUtils().getPotentialBots().clear()));
        this.buttonList.add(new ActionButton<Void>(3, (this.width / 2) + this.scrollingTextXOffsetFromMiddle, ((this.height + this.scrollingTextHeight) / 2) + this.buttonYSeparation, this.scrollingTextWidth, this.buttonHeight, "Clear Player Cache", nothing -> this.mod.getPlayerValidator().getPlayerCache().clear()));
    }

    @Override
    public void updateScreen() {
        final List<List<String>> potentialBotNames = this.mod.getBotUtils().getPotentialBots().stream().map(playerId -> Arrays.asList(playerId.getName().orElse(""), playerId.getId().isPresent() ? playerId.getId().get().toString() : "")).collect(Collectors.toList());
        final List<List<String>> playerCacheNames = this.mod.getPlayerValidator().getPlayerCache().entrySet().stream().map(playerIdAccountTypeEntry -> Arrays.asList(playerIdAccountTypeEntry.getKey().getName().orElse(""), playerIdAccountTypeEntry.getKey().getId().isPresent() ? playerIdAccountTypeEntry.getKey().getId().get().toString() : "", playerIdAccountTypeEntry.getValue().toString())).collect(Collectors.toList());

        if (this.potentialBotsNameListSize != potentialBotNames.size()) {
            this.potentialBotsNameListSize = potentialBotNames.size();
            this.potentialBotsNameList = new GuiScrollingTextList(this.scrollingTextWidth, this.scrollingTextHeight, (this.height - this.scrollingTextHeight) / 2, (this.height + this.scrollingTextHeight) / 2, (this.width / 2) - this.scrollingTextWidth - this.scrollingTextXOffsetFromMiddle, this.scrollingTextYIncrement * 2 + this.scrollingTextAdditionalYMargin, this.width, this.height, this, this.scrollingTextYIncrement, potentialBotNames);
        }

        if (this.playerCacheNameListSize != playerCacheNames.size()) {
            this.playerCacheNameListSize = playerCacheNames.size();
            this.playerCacheNameList = new GuiScrollingTextList(this.scrollingTextWidth, this.scrollingTextHeight, (this.height - this.scrollingTextHeight) / 2, (this.height + this.scrollingTextHeight) / 2, (this.width / 2) + this.scrollingTextXOffsetFromMiddle, this.scrollingTextYIncrement * 3 + this.scrollingTextAdditionalYMargin, this.width, this.height, this, this.scrollingTextYIncrement, playerCacheNames);
        }
    }

    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        this.potentialBotsNameList.drawScreen(mouseX, mouseY, partialTicks);
        this.playerCacheNameList.drawScreen(mouseX, mouseY, partialTicks);

        if (!this.buttonList.isEmpty()) {
            this.drawCenteredString(this.fontRendererObj, "Request Cooldown: " + (Optional.ofNullable(this.mod.getRequestCooldown()).isPresent() ? this.mod.getRequestCooldown().getRemainingMillis() / 1000L : 0L) + "s", this.width / 2, this.buttonList.get(1).yPosition + this.buttonHeight + this.buttonYSeparation, IgnoredogGUI.WHITE);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(final GuiButton button) {
        if (button instanceof ActionButton) {
            ((ActionButton) button).onPress();
        }
    }
}
