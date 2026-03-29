package com.unrealdinnerbone.weathergate.client.screen;

import com.unrealdinnerbone.weathergate.modifers.base.TerrainModifier;
import dev.ftb.mods.ftblibrary.client.gui.theme.Theme;
import dev.ftb.mods.ftblibrary.client.gui.widget.BaseScreen;
import dev.ftb.mods.ftblibrary.client.gui.widget.ModalPanel;
import dev.ftb.mods.ftblibrary.client.gui.widget.Panel;
import dev.ftb.mods.ftblibrary.client.gui.widget.SimpleButton;
import dev.ftb.mods.ftblibrary.client.icon.IconHelper;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.icon.Icons;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

public class EditButton<B> extends SimpleButton {
    private final TerrainModifier<B> terrainModifier;
    private B activeValue;
    private final Component name;

    public EditButton(TerrainControllerScreen screen2, Panel panel, TerrainModifier<B> terrainModifier, B activeValue, Consumer<B> newValueApplier) {
        this.terrainModifier = terrainModifier;
        this.activeValue = activeValue;
        Component name = Component.translatable(terrainModifier.id().toLanguageKey() + ".info");
        super(panel, name, Icons.ART, (a, b) -> {});
        this.name = Component.translatable(terrainModifier.id().toLanguageKey());
        setConsumer((button, mouseButton) -> {
            ModalPanel editPanel = terrainModifier.createEditPanel(screen2, this.activeValue, newValueApplier.andThen(newValue -> this.activeValue = newValue));
            BaseScreen gui = editPanel.getGui();
            int absX = Math.min(gui.getMouseX(), gui.getWindow().getGuiScaledWidth() - editPanel.width - 10);
            int absY = Math.min(gui.getMouseY(), gui.getWindow().getGuiScaledHeight() - editPanel.height - 10);
            editPanel.setPos(absX - editPanel.getParent().getX(), absY - editPanel.getParent().getY());
            editPanel.getGui().pushModalPanel(editPanel);
            editPanel.setExtraZlevel(100);
        });
    }

    @Override
    public void draw(GuiGraphicsExtractor graphics, Theme theme, int x, int y, int w, int h) {
        super.draw(graphics, theme, x, y, w, h);
        theme.drawWidget(graphics, x, y, w, h, getWidgetType());
        IconHelper.renderIcon(Color4I.BLACK.withAlpha(125), graphics, x + 2, y + 2, 12, 12);
        IconHelper.renderIcon(this.terrainModifier.getIcon(this.activeValue), graphics, x + 2, y + 2, 12, 12);
//            IconHelper.renderIcon(icon, graphics, x + 3, y + 3, 10, 10);
        theme.drawString(graphics, name, x + 18, y + 4);
    }

}