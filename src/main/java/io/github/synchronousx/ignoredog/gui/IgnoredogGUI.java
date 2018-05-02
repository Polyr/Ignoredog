package io.github.synchronousx.ignoredog.gui;

import io.github.synchronousx.ignoredog.IgnoredogMod;
import io.github.synchronousx.ignoredog.gui.buttons.ToggleButton;
import io.github.synchronousx.ignoredog.gui.buttons.ToggleButton.Status;
import io.github.synchronousx.ignoredog.utils.Logger;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.util.ArrayList;
import java.util.List;

public class IgnoredogGUI extends GuiScreen {
    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 20;
    private static final int BUTTON_SEPARATION = 2;
    private final ToggleButton modButton;
    private final ToggleButton debugButton;
    private final IgnoredogMod mod;
    private final List<GuiButton> buttons = new ArrayList<>();

    public IgnoredogGUI(final IgnoredogMod mod) {
        this.mod = mod;
        this.modButton = new ToggleButton(0, 0, 0, IgnoredogGUI.BUTTON_WIDTH, IgnoredogGUI.BUTTON_HEIGHT, "Ignoredog Mod", this.mod.isEnabled() ? Status.ENABLED : Status.DISABLED);
        this.debugButton = new ToggleButton(1, 0, 0, IgnoredogGUI.BUTTON_WIDTH, IgnoredogGUI.BUTTON_HEIGHT, "Debug Messages", this.mod.sendDebugMessages() ? Status.ENABLED : Status.DISABLED);
        this.buttons.add(this.modButton);
        this.buttons.add(this.debugButton);
    }

    private void centerButtons() {
        final int x = (this.width - IgnoredogGUI.BUTTON_WIDTH) / 2;
        int y = (this.height - (this.buttons.size() * (IgnoredogGUI.BUTTON_HEIGHT + IgnoredogGUI.BUTTON_SEPARATION) + IgnoredogGUI.BUTTON_SEPARATION)) / 2;

        for (final GuiButton button : this.buttons) {
            button.xPosition = x;
            button.yPosition = (y += IgnoredogGUI.BUTTON_HEIGHT + IgnoredogGUI.BUTTON_SEPARATION);
        }
    }

    @Override
    public void initGui() {
        centerButtons();
        this.buttonList.addAll(this.buttons);
    }

    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(final GuiButton button) {
        if (button == this.modButton) {
            this.modButton.onPress(status -> {
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
            });
        } else if (button == this.debugButton) {
            this.debugButton.onPress(status -> {
                switch (status) {
                    case ENABLED:
                        this.mod.setDebugMessages(true);
                        break;
                    case DISABLED:
                        this.mod.setDebugMessages(false);
                }
            });
        }
    }
}
