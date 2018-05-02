package io.github.synchronousx.ignoredog.gui.buttons;

import io.github.synchronousx.ignoredog.gui.buttons.ToggleButton.Status;
import net.minecraft.util.EnumChatFormatting;

import java.util.function.Consumer;

public class ToggleButton extends ActionButton<Status> {
    private final String prefix;
    private Status status = Status.DISABLED;

    public ToggleButton(final int id, final int xPos, final int yPos, final String prefix) {
        super(id, xPos, yPos, null);
        this.prefix = prefix;
        this.updateDisplayString();
    }

    public ToggleButton(final int id, final int xPos, final int yPos, final String prefix, final Status status) {
        super(id, xPos, yPos, null);
        this.prefix = prefix;
        this.status = status;
        this.updateDisplayString();
    }

    public ToggleButton(final int id, final int xPos, final int yPos, final int width, final int height, final String prefix) {
        super(id, xPos, yPos, width, height, null);
        this.prefix = prefix;
        this.updateDisplayString();
    }

    public ToggleButton(final int id, final int xPos, final int yPos, final int width, final int height, final String prefix, final Status status) {
        super(id, xPos, yPos, width, height, null);
        this.prefix = prefix;
        this.status = status;
        this.updateDisplayString();
    }

    private void updateDisplayString() {
        this.displayString = this.prefix + ": " + this.status.getText();
    }

    public String getPrefix() {
        return prefix;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(final Status status) {
        this.status = status;
    }

    @Override
    public void onPress(final Consumer<Status> action) {
        switch (this.status) {
            case ENABLED:
                this.status = Status.DISABLED;
                break;
            case DISABLED:
                this.status = Status.ENABLED;
        }

        this.updateDisplayString();
        action.accept(this.status);
    }

    public enum Status {
        ENABLED(EnumChatFormatting.GREEN + "ENABLED"),
        DISABLED(EnumChatFormatting.RED + "DISABLED");

        private final String text;

        Status(final String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }
    }
}
