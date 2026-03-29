package com.unrealdinnerbone.weathergate.modifers.base;

import com.mojang.serialization.Codec;
import com.unrealdinnerbone.weathergate.WeatherGateCodecs;
import com.unrealdinnerbone.weathergate.client.screen.TerrainControllerScreen2;
import com.unrealdinnerbone.weathergate.level.attachments.TerrainControllerAttachment;
import com.unrealdinnerbone.weathergate.network.packets.c2s.UpdateControllerPacket;
import dev.ftb.mods.ftblibrary.client.config.editable.EditableColor;
import dev.ftb.mods.ftblibrary.client.gui.theme.Theme;
import dev.ftb.mods.ftblibrary.client.gui.widget.ColorSelectorPanel;
import dev.ftb.mods.ftblibrary.client.gui.widget.Panel;
import dev.ftb.mods.ftblibrary.client.gui.widget.SimpleButton;
import dev.ftb.mods.ftblibrary.client.icon.IconHelper;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.icon.Icons;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

import java.util.Map;

public abstract class AbstactColorTerrainModifier implements TerrainModifier<Color4I> {

    @Override
    public SimpleButton createButton(Panel basePane, TerrainControllerScreen2 basePanel, Panel panel, BlockPos blockPos, TerrainControllerAttachment.ModifierState<Color4I> state) {
        return new MySimpleButton(basePane, basePanel, panel, blockPos, state);
    }

    @Override
    public Codec<Color4I> getCodec() {
        return WeatherGateCodecs.COLOR4I_CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, Color4I> getStreamCodec() {
        return WeatherGateCodecs.COLOR4I_STREAM_CODEC;
    }

    public boolean supportsAlpha() {
        return false;
    }

    private class MySimpleButton extends SimpleButton {
        private Color4I activeValue;
        private final Component name;

        public MySimpleButton(Panel basePanel, TerrainControllerScreen2 screen2, Panel panel, BlockPos blockPos, TerrainControllerAttachment.ModifierState<Color4I> activeValue) {
            this.activeValue = activeValue.value();
            Component name = Component.translatable(AbstactColorTerrainModifier.this.id().toLanguageKey());
            super(panel, name, Icons.ART, (a, b) -> {});
            this.name = name;
            setConsumer((button, mouseButton) -> {
                EditableColor config = new EditableColor();
                ColorSelectorPanel colorSelectorPanel = new ColorSelectorPanel(basePanel, config, (accepted) -> {
                    if(accepted) {
                        MySimpleButton.this.activeValue = config.getValue();
                        ClientPacketDistributor.sendToServer(new UpdateControllerPacket(GlobalPos.of(Minecraft.getInstance().player.level().dimension(), blockPos), new TerrainControllerAttachment.StoredData(Map.of(AbstactColorTerrainModifier.this, new TerrainControllerAttachment.ModifierState<>(activeValue.enabled(), config.getValue())))));
                        if (AbstactColorTerrainModifier.this.requireReRender()) {
                            Minecraft.getInstance().levelRenderer.allChanged();
                        }
                        screen2.data.modifiers().put(AbstactColorTerrainModifier.this, activeValue.withValue(config.getValue()));
                        screen2.refreshWidgets();
                    }
                });
                colorSelectorPanel.setPos(basePanel.width / 2 - colorSelectorPanel.width / 2, basePanel.width / 2 - colorSelectorPanel.height / 2);
                colorSelectorPanel.setAllowAlphaEdit(AbstactColorTerrainModifier.this.supportsAlpha());
                colorSelectorPanel.getGui().pushModalPanel(colorSelectorPanel);
                colorSelectorPanel.setExtraZlevel(100);
            });
            this.activeValue = activeValue.value();
        }

        @Override
        public void drawIcon(GuiGraphicsExtractor graphics, Theme theme, int x, int y, int w, int h) {
//            IconHelper.renderIcon(this.icon, graphics, x, y, w, h);
        }

        @Override
        public void draw(GuiGraphicsExtractor graphics, Theme theme, int x, int y, int w, int h) {
            super.draw(graphics, theme, x, y, w, h);
            theme.drawWidget(graphics, x, y, w, h, getWidgetType());
            IconHelper.renderIcon(Color4I.BLACK.withAlpha(125), graphics, x + 2, y + 2 , 12, 12);
            IconHelper.renderIcon(activeValue, graphics, x + 2, y + 2, 12, 12);
//            IconHelper.renderIcon(icon, graphics, x + 3, y + 3, 10, 10);
            theme.drawString(graphics, name, x + 18, y + 4);
        }

    }
}
