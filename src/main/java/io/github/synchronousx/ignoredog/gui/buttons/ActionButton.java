package io.github.synchronousx.ignoredog.gui.buttons;

import net.minecraftforge.fml.client.config.GuiButtonExt;

import java.util.function.Consumer;

public abstract class ActionButton<T> extends GuiButtonExt {
    public ActionButton(final int id, final int xPos, final int yPos, final String displayString) {
        super(id, xPos, yPos, displayString);
    }

    public ActionButton(final int id, final int xPos, final int yPos, final int width, final int height, final String displayString) {
        super(id, xPos, yPos, width, height, displayString);
    }

    public abstract void onPress(final Consumer<T> action);
}
