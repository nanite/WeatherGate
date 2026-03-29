package com.unrealdinnerbone.weathergate.client.screen;

import dev.ftb.mods.ftblibrary.client.gui.theme.Theme;
import dev.ftb.mods.ftblibrary.client.gui.widget.Panel;
import dev.ftb.mods.ftblibrary.client.gui.widget.SimpleButton;
import dev.ftb.mods.ftblibrary.icon.Icon;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;

import java.util.List;

public class BetterButton extends SimpleButton {

    public BetterButton(Panel panel, Component text, Icon<?> icon, Callback c) {
        super(panel, text, icon, c);
    }

    public BetterButton(Panel panel, List<Component> text, Icon<?> icon, Callback c) {
        super(panel, text, icon, c);
    }

    public static BetterButton simple(Panel panel, Component text, Icon<?> icon, Runnable callback) {
        return new BetterButton(panel, text, icon, (_, _) -> callback.run());
    }

    @Override
    public void draw(GuiGraphicsExtractor gfx, Theme theme, int x, int y, int w, int h) {
        theme.drawWidget(gfx, x, y, w, h, getWidgetType());
        super.draw(gfx, theme, x, y, w, h);
    }
}
