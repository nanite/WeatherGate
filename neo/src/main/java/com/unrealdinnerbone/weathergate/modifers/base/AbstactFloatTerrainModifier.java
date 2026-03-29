package com.unrealdinnerbone.weathergate.modifers.base;

import com.mojang.serialization.Codec;
import com.unrealdinnerbone.weathergate.WeatherGateCodecs;
import com.unrealdinnerbone.weathergate.client.screen.FloatEditPanel;
import dev.ftb.mods.ftblibrary.client.gui.widget.ModalPanel;
import dev.ftb.mods.ftblibrary.client.gui.widget.Panel;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.icon.Icons;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.function.Consumer;

public abstract class AbstactFloatTerrainModifier implements TerrainModifier<Float> {

    @Override
    public Codec<Float> getCodec() {
        return Codec.FLOAT;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, Float> getStreamCodec() {
        return WeatherGateCodecs.FLOAT;
    }

    @Override
    public ModalPanel createEditPanel(Panel basePanel, Float activeValue, Consumer<Float> newValueApplier) {
        FloatEditPanel floatEditPanel = new FloatEditPanel(basePanel, activeValue, newValueApplier);
        floatEditPanel.setLimits(getMinValue(), getMaxValue());
        return floatEditPanel;
    }

    @Override
    public Icon<?> getIcon(Float value) {
        return Icons.INFO;
    }

    public abstract float getMinValue();

    public abstract float getMaxValue();
}
