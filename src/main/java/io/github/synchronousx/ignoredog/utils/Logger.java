package io.github.synchronousx.ignoredog.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Pattern;

public class Logger {
    private static final char[] FORMAT_CHARACTERS = Logger.getFormatCharacters();
    private static final char AMPERSAND = '&';
    private static final char SECTION_SIGN = '\u00A7';
    private static final Pattern AMPERSAND_FORMAT_CODE = Pattern.compile(Logger.AMPERSAND + "(?=[" + String.valueOf(Logger.FORMAT_CHARACTERS) + "])", Pattern.CASE_INSENSITIVE);
    private static final String RESET_CODE = Logger.translateAmpersandFormatting("&f");
    private static final String PREFIX = Logger.translateAmpersandFormatting("&d&l[&rIgnoredog&d&l]&r ");

    private static char[] getFormatCharacters() {
        final char[] formatCharacters = new char[EnumChatFormatting.values().length];
        Arrays.stream(EnumChatFormatting.values()).forEach(enumChatFormatting -> formatCharacters[enumChatFormatting.ordinal()] = enumChatFormatting.toString().charAt(1));
        return formatCharacters;
    }

    public static String translateAmpersandFormatting(final String message) {
        return Logger.AMPERSAND_FORMAT_CODE.matcher(message).replaceAll(String.valueOf(Logger.SECTION_SIGN));
    }

    public static void log(final Object message) {
        Optional.ofNullable(Minecraft.getMinecraft()).ifPresent(minecraft -> Optional.ofNullable(minecraft.thePlayer).ifPresent(player -> player.addChatMessage(new ChatComponentText((Logger.PREFIX + message).replace(EnumChatFormatting.RESET.toString(), Logger.RESET_CODE)))));
    }
}
