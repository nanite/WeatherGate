package com.unrealdinnerbone.weathergate.client.screen;

import dev.ftb.mods.ftblibrary.client.gui.input.Key;
import dev.ftb.mods.ftblibrary.client.gui.input.MouseButton;
import dev.ftb.mods.ftblibrary.client.gui.theme.Theme;
import dev.ftb.mods.ftblibrary.client.gui.widget.BaseScreen;
import dev.ftb.mods.ftblibrary.client.gui.widget.IntTextBox;
import dev.ftb.mods.ftblibrary.client.gui.widget.ModalPanel;
import dev.ftb.mods.ftblibrary.client.gui.widget.Panel;
import dev.ftb.mods.ftblibrary.client.gui.widget.SimpleButton;
import dev.ftb.mods.ftblibrary.client.icon.IconHelper;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.icon.Icons;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

public class IntEditPanel extends ModalPanel {
    private final IntTextBox valueBox;
    private final SimpleButton applyButton;
    private final SimpleButton cancelButton;
    private final Consumer<Integer> onApply;

    public IntEditPanel(Panel parent, int currentValue, Consumer<Integer> onApply) {
        super(parent);
        this.onApply = onApply;

        setWidth(120);
        setHeight(32 + 16);

        valueBox = new IntTextBox(this);
        valueBox.setAmount(currentValue);

        applyButton = new SimpleButton(this, Component.literal("Apply"), Icons.ACCEPT, (btn, mb) -> {
            this.onApply.accept(valueBox.getIntValue());
            this.getGui().popModalPanel();
        });

        cancelButton = new SimpleButton(this, Component.literal("Cancel"), Icons.CANCEL, (btn, mb) -> this.getGui().popModalPanel());
    }

    @Override
    public void addWidgets() {
        add(valueBox);
        add(applyButton);
        add(cancelButton);
    }

    @Override
    public void alignWidgets() {
        valueBox.setPos(4, 4);
        valueBox.setSize(width - 8, 14);

        applyButton.setPos(width - 16 - 16 - 4 - 4, 22);
        applyButton.setSize(16, 16);

        cancelButton.setPos(width - 16 - 4, 22);
        cancelButton.setSize(16, 16);
    }

    public void setLimits(int min, int max) {
        valueBox.setMax(max);
        valueBox.setMin(min);
    }

    public void drawBackground(GuiGraphicsExtractor graphics, Theme theme, int x, int y, int w, int h) {
        theme.drawContextMenuBackground(graphics, x - 1, y - 1, w + 2, h + 2);
        IconHelper.renderIcon(Color4I.GRAY.withAlpha(40), graphics, x + 130, y + 43, 50, 50);
    }

    public void draw(GuiGraphicsExtractor graphics, Theme theme, int x, int y, int w, int h) {
        super.draw(graphics, theme, x, y, w, h);
//        theme.drawString(graphics, this.allowAlphaEdit ? ARGB : RGB, x + 157 - theme.getStringWidth(ARGB), y + 9);
    }

    public boolean keyPressed(Key key) {
        if (key.esc()) {
            cancelButton.onClicked(MouseButton.LEFT);
            return true;
        } else if (key.enter() && BaseScreen.isShiftKeyDown()) {
            applyButton.onClicked(MouseButton.LEFT);
            return true;
        } else {
            return super.keyPressed(key);
        }
    }
//
//    private void closeSelf() {
//        // Depending on FTB Library version, use whichever is available:
////        removeFromParent(); // if supported
//        // or: getGui().closeModalPanel(this);
//    }
}
