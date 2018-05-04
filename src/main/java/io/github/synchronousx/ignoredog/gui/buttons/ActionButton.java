package io.github.synchronousx.ignoredog.gui.buttons;

import net.minecraftforge.fml.client.config.GuiButtonExt;

import java.util.function.Consumer;

public class ActionButton<T> extends GuiButtonExt {
    protected final Consumer<T> action;

    public ActionButton(final int id, final int xPos, final int yPos, final String displayString, final Consumer<T> action) {
        super(id, xPos, yPos, displayString);
        this.action = action;
    }

    public ActionButton(final int id, final int xPos, final int yPos, final int width, final int height, final String displayString, final Consumer<T> action) {
        super(id, xPos, yPos, width, height, displayString);
        this.action = action;
    }

    public void onPress() {
        this.action.accept(null);
    }
}
