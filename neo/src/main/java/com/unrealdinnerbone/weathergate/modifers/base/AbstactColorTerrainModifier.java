package com.unrealdinnerbone.weathergate.modifers.base;

import com.mojang.serialization.Codec;
import com.unrealdinnerbone.weathergate.WeatherGateCodecs;
import dev.ftb.mods.ftblibrary.client.config.editable.EditableColor;
import dev.ftb.mods.ftblibrary.client.gui.widget.ColorSelectorPanel;
import dev.ftb.mods.ftblibrary.client.gui.widget.ModalPanel;
import dev.ftb.mods.ftblibrary.client.gui.widget.Panel;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.icon.Icon;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.function.Consumer;

public abstract class AbstactColorTerrainModifier implements TerrainModifier<Color4I> {

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

    @Override
    public ModalPanel createEditPanel(Panel basePanel, Color4I activeValue, Consumer<Color4I> newValueApplier) {
        EditableColor config = new EditableColor();
        ColorSelectorPanel colorSelectorPanel = new ColorSelectorPanel(basePanel, config, (accepted) -> {
            if(accepted) {
               newValueApplier.accept(config.getValue());
            }
        });
        colorSelectorPanel.setAllowAlphaEdit(this.supportsAlpha());
        return colorSelectorPanel;
    }

    @Override
    public Icon<?> getIcon(Color4I value) {
        return value;
    }
}
