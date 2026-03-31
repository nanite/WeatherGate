package com.unrealdinnerbone.weathergate.modifers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.unrealdinnerbone.weathergate.WeatherGateCodecs;
import com.unrealdinnerbone.weathergate.modifers.base.EnvironmentAttributeModifier;
import com.unrealdinnerbone.weathergate.modifers.base.TerrainModifier;
import com.unrealdinnerbone.weathergate.modifers.types.TerrainModifierType;
import dev.ftb.mods.ftblibrary.client.config.editable.EditableColor;
import dev.ftb.mods.ftblibrary.client.gui.widget.ColorSelectorPanel;
import dev.ftb.mods.ftblibrary.client.gui.widget.ModalPanel;
import dev.ftb.mods.ftblibrary.client.gui.widget.Panel;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.icon.Icon;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.attribute.EnvironmentAttribute;

import java.util.function.Consumer;

public class AlphaColorModifier implements TerrainModifier<Color4I> {

    private final TerrainModifierType<Color4I, AlphaColorModifier> type;
    private final Color4I value;
    private final EnvironmentAttribute<Integer> attribute;
    private final boolean enabled;

    public record Data(Color4I value, boolean enabled) {

        public static final MapCodec<Data> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                WeatherGateCodecs.COLOR4I_ALPAH_CODEC.fieldOf("value").forGetter(Data::value),
                Codec.BOOL.fieldOf("enabled").forGetter(Data::enabled)
        ).apply(instance, Data::new));

        public static final StreamCodec<? super ByteBuf, Data> STREAM_CODEC = StreamCodec.composite(
                WeatherGateCodecs.COLOR4I_ALPHA_STREAM_CODEC,
                Data::value,
                ByteBufCodecs.BOOL,
                Data::enabled,
                Data::new
        );
    }

    public AlphaColorModifier(TerrainModifierType<Color4I, AlphaColorModifier> type, Color4I value, EnvironmentAttribute<Integer> attribute, boolean enabled) {
        this.type = type;
        this.value = value;
        this.attribute = attribute;
        this.enabled = enabled;
    }

    @Override
    public TerrainModifierType<Color4I, ?> type() {
        return this.type;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public ModalPanel createEditPanel(Panel basePanel, Color4I activeValue, Consumer<Color4I> newValueApplier) {
        EditableColor config = new EditableColor();
        config.updateValue(activeValue);
        ColorSelectorPanel colorSelectorPanel = new ColorSelectorPanel(basePanel, config, (accepted) -> {
            if (accepted) {
                newValueApplier.accept(config.getValue());
            }
        });

        colorSelectorPanel.setAllowAlphaEdit(true);
        return colorSelectorPanel;
    }

    public Data data() {
        return new Data(value, enabled);
    }

    @Override
    public Color4I getValue() {
        return value;
    }

    @Override
    public Icon<?> getIcon(Color4I value) {
        return value;
    }

    @Override
    public TerrainModifier<Color4I> withValue(Color4I value) {
        return new AlphaColorModifier(type, value, attribute, enabled);
    }

    @Override
    public TerrainModifier<Color4I> withEnabled(boolean enabled) {
        return new AlphaColorModifier(type, value, attribute, enabled);
    }


}

