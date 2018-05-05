package io.github.synchronousx.ignoredog.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.client.GuiScrollingList;

import java.util.List;

public class GuiScrollingTextList extends GuiScrollingList {
    private static final int WHITE = 16777215;
    private final GuiScreen parent;
    private final int textYIncrement;
    private final List<List<String>> textList;

    public GuiScrollingTextList(final int width, final int height, final int top, final int bottom, final int left, final int entryHeight, final int screenWidth, final int screenHeight, final GuiScreen parent, final int textYIncrement, final List<List<String>> textList) {
        super(Minecraft.getMinecraft(), width, height, top, bottom, left, entryHeight, screenWidth, screenHeight);
        this.parent = parent;
        this.textYIncrement = textYIncrement;
        this.textList = textList;
    }

    @Override
    protected int getSize() {
        return this.textList.size();
    }

    @Override
    protected void elementClicked(int index, boolean doubleClick) {
    }

    @Override
    protected boolean isSelected(int index) {
        return false;
    }

    @Override
    protected void drawBackground() {
    }

    @Override
    protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess) {
        int y = slotTop;
        for (final String text : this.textList.get(slotIdx)) {
            this.parent.drawCenteredString(this.parent.mc.fontRendererObj, text, this.left + this.listWidth / 2, y, GuiScrollingTextList.WHITE);
            y += this.textYIncrement;
        }
    }
}
