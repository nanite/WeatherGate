package com.unrealdinnerbone.weathergate.modifers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.unrealdinnerbone.weathergate.client.screen.FloatEditPanel;
import com.unrealdinnerbone.weathergate.modifers.base.TerrainModifier;
import com.unrealdinnerbone.weathergate.modifers.types.FloatEnvironmentType;
import com.unrealdinnerbone.weathergate.modifers.types.TerrainModifierType;
import dev.ftb.mods.ftblibrary.client.gui.widget.ModalPanel;
import dev.ftb.mods.ftblibrary.client.gui.widget.Panel;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.icon.Icons;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.attribute.EnvironmentAttribute;

import java.util.function.Consumer;

public class FloatModifier implements TerrainModifier<Float> {

    private final FloatEnvironmentType type;
    private final float value;
    private final EnvironmentAttribute<Float> attribute;
    private final boolean enabled;

    public record Data(float value, boolean enabled) {

        public static final MapCodec<Data> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.FLOAT.fieldOf("value").forGetter(Data::value),
                Codec.BOOL.fieldOf("enabled").forGetter(Data::enabled)
        ).apply(instance, Data::new));

        public static final StreamCodec<? super ByteBuf, Data> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.FLOAT,
                Data::value,
                ByteBufCodecs.BOOL,
                Data::enabled,
                Data::new
        );
    }

    public FloatModifier(FloatEnvironmentType type, float value, EnvironmentAttribute<Float> attribute, boolean enabled) {
        this.type = type;
        this.value = value;
        this.attribute = attribute;
        this.enabled = enabled;
    }

    public Data data() {
        return new Data(value, enabled);
    }

    @Override
    public TerrainModifierType<Float, ?> type() {
        return this.type;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public ModalPanel createEditPanel(Panel basePanel, Float activeValue, Consumer<Float> newValueApplier) {
        FloatEditPanel floatEditPanel = new FloatEditPanel(basePanel, activeValue, newValueApplier);
        floatEditPanel.setLimits(this.type.getMin(), this.type.getMax());
        return floatEditPanel;
    }

    @Override
    public Float getValue() {
        return value;
    }

    @Override
    public Icon<?> getIcon(Float value) {
        return Icons.INFO;
    }

    public float value() {
        return this.value;
    }

    @Override
    public TerrainModifier<Float> withValue(Float value) {
        return new FloatModifier(type, value, attribute, enabled);
    }

    @Override
    public TerrainModifier<Float> withEnabled(boolean enabled) {
        return new FloatModifier(type, value, attribute, enabled);
    }

}
