package com.unrealdinnerbone.weathergate.modifers.types;

import com.mojang.serialization.MapCodec;
import com.unrealdinnerbone.weathergate.modifers.FloatModifier;
import com.unrealdinnerbone.weathergate.modifers.base.EnvironmentAttributeModifier;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

public class FloatEnvironmentType extends TerrainModifierType<Float, FloatModifier> implements EnvironmentAttributeModifier<Float, Float> {

    private final EnvironmentAttribute<Float> attribute;
    private final MapCodec<FloatModifier> codec;
    private final StreamCodec<? super RegistryFriendlyByteBuf, FloatModifier> streamCodec;
    private final float min;
    private final float max;

    private FloatEnvironmentType(EnvironmentAttribute<Float> attribute, float min, float max) {
        this.attribute = attribute;
        this.min = min;
        this.max = max;
        this.codec = createCodec();
        this.streamCodec = createStreamCodec();
    }

    private MapCodec<FloatModifier> createCodec() {
        return FloatModifier.Data.CODEC.xmap(data -> new FloatModifier(this, data.value(), this.attribute, data.enabled()), FloatModifier::data).fieldOf("value");
    }

    private StreamCodec<? super ByteBuf, FloatModifier> createStreamCodec() {
        return FloatModifier.Data.STREAM_CODEC.map(data -> new FloatModifier(this, data.value(), this.attribute, data.enabled()), FloatModifier::data);
    }

    public static FloatEnvironmentType of(EnvironmentAttribute<Float> attribute, float minValue, float maxValue) {
        return new FloatEnvironmentType(attribute, minValue, maxValue);
    }

    public static FloatEnvironmentType of(EnvironmentAttribute<Float> attribute) {
        return of(attribute, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY);
    }

    public static FloatEnvironmentType nonNegative(EnvironmentAttribute<Float> attribute) {
        return of(attribute, 0, Float.POSITIVE_INFINITY);
    }

    @Override
    public MapCodec<FloatModifier> codec() {
        return codec;
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, FloatModifier> streamCodec() {
        return streamCodec;
    }

    @Override
    public FloatModifier create(Level level, Biome biome, BlockPos pos) {
        return new FloatModifier(this, level.environmentAttributes().getValue(attribute, pos), attribute, false);
    }

    @Override
    public Float getAttributeValue(Float value) {
        return value;
    }

    @Override
    public EnvironmentAttribute<Float> attribute() {
        return attribute;
    }

    public float getMax() {
        return max;
    }

    public float getMin() {
        return min;
    }
}
