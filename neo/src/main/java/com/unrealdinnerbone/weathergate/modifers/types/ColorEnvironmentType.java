package com.unrealdinnerbone.weathergate.modifers.types;

import com.mojang.serialization.MapCodec;
import com.unrealdinnerbone.weathergate.modifers.ColorModifier;
import com.unrealdinnerbone.weathergate.modifers.FloatModifier;
import com.unrealdinnerbone.weathergate.modifers.base.EnvironmentAttributeModifier;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

public class ColorEnvironmentType extends TerrainModifierType<Color4I, ColorModifier> implements EnvironmentAttributeModifier<Color4I, Integer> {

    private final EnvironmentAttribute<Integer> attribute;
    private final MapCodec<ColorModifier> codec;
    private final StreamCodec<? super RegistryFriendlyByteBuf, ColorModifier> streamCodec;

    private ColorEnvironmentType(EnvironmentAttribute<Integer> attribute) {
        this.attribute = attribute;
        this.codec = createCodec();
        this.streamCodec = createStreamCodec();
    }

    private MapCodec<ColorModifier> createCodec() {
        return ColorModifier.Data.CODEC.xmap(data -> new ColorModifier(this, data.value(), attribute, data.enabled()), ColorModifier::data).fieldOf("value");
    }

    private StreamCodec<? super ByteBuf, ColorModifier> createStreamCodec() {
        return ColorModifier.Data.STREAM_CODEC.map(data -> new ColorModifier(this, data.value(), attribute, data.enabled()),  ColorModifier::data);
    }

    public static ColorEnvironmentType of(EnvironmentAttribute<Integer> attribute) {
        return new ColorEnvironmentType(attribute);
    }

    @Override
    public MapCodec<ColorModifier> codec() {
        return codec;
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, ColorModifier> streamCodec() {
        return streamCodec;
    }

    @Override
    public ColorModifier create(Level level, Biome biome, BlockPos pos) {
        Integer value = level.environmentAttributes().getValue(attribute, pos);
        return new ColorModifier(this, Color4I.rgb(value), attribute, false);
    }

    @Override
    public Integer getAttributeValue(Color4I value) {
        return value.rgb();
    }

    @Override
    public EnvironmentAttribute<Integer> attribute() {
        return attribute;
    }
}
