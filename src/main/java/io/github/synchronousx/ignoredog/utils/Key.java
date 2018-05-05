package io.github.synchronousx.ignoredog.utils;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

import java.util.Arrays;

public enum Key {
    OPEN_GUI("key.ignoredog.openGui", Keyboard.KEY_I, Key.CATEGORY);

    public static final String CATEGORY = "key.categories.ignoredog";
    private final KeyBinding keyBinding;

    Key(final String description, final int keyCode, final String category) {
        this.keyBinding = new KeyBinding(description, keyCode, category);
    }

    public static void registerKeyBindings() {
        Arrays.stream(Key.values()).forEach(Key::registerKeyBinding);
    }

    public void registerKeyBinding() {
        ClientRegistry.registerKeyBinding(this.keyBinding);
    }

    public KeyBinding getKeyBinding() {
        return this.keyBinding;
    }
}
