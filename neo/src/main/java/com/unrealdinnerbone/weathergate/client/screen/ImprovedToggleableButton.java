package com.unrealdinnerbone.weathergate.client.screen;

import dev.ftb.mods.ftblibrary.client.gui.theme.Theme;
import dev.ftb.mods.ftblibrary.client.gui.widget.Panel;
import dev.ftb.mods.ftblibrary.client.gui.widget.ToggleableButton;
import dev.ftb.mods.ftblibrary.icon.Icon;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public class ImprovedToggleableButton extends ToggleableButton {

    public ImprovedToggleableButton(Panel panel, boolean defaultState, Icon<?> enabled, Icon<?> disabled, ToggleableCallback toggleableCallback) {
        super(panel, defaultState, enabled, disabled, toggleableCallback);
    }

    public ImprovedToggleableButton(Panel panel, boolean defaultState, ToggleableCallback toggleableCallback) {
        super(panel, defaultState, toggleableCallback);
    }

    @Override
    public void draw(GuiGraphicsExtractor gfx, Theme theme, int x, int y, int w, int h) {
        theme.drawWidget(gfx, x, y, w, h, getWidgetType());
        super.draw(gfx, theme, x, y, w, h);
    }
}
