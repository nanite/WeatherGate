package com.unrealdinnerbone.weathergate.modifers.types;

import com.mojang.serialization.MapCodec;
import com.unrealdinnerbone.weathergate.modifers.AlphaColorModifier;
import com.unrealdinnerbone.weathergate.modifers.base.EnvironmentAttributeModifier;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

public class AlphaColorEnvironmentType extends TerrainModifierType<Color4I, AlphaColorModifier> implements EnvironmentAttributeModifier<Color4I, Integer> {

    private final EnvironmentAttribute<Integer> attribute;
    private final MapCodec<AlphaColorModifier> codec;
    private final StreamCodec<? super RegistryFriendlyByteBuf, AlphaColorModifier> streamCodec;

    private AlphaColorEnvironmentType(EnvironmentAttribute<Integer> attribute) {
        this.attribute = attribute;
        this.codec = createCodec();
        this.streamCodec = createStreamCodec();
    }

    private MapCodec<AlphaColorModifier> createCodec() {
        return AlphaColorModifier.Data.CODEC.xmap(data -> new AlphaColorModifier(this, data.value(), attribute, data.enabled()), AlphaColorModifier::data).fieldOf("value");
    }

    private StreamCodec<? super ByteBuf, AlphaColorModifier> createStreamCodec() {
        return AlphaColorModifier.Data.STREAM_CODEC.map(data -> new AlphaColorModifier(this, data.value(), attribute, data.enabled()), AlphaColorModifier::data);
    }

    public static AlphaColorEnvironmentType of(EnvironmentAttribute<Integer> attribute) {
        return new  AlphaColorEnvironmentType(attribute);
    }

    @Override
    public MapCodec<AlphaColorModifier> codec() {
        return codec;
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, AlphaColorModifier> streamCodec() {
        return streamCodec;
    }

    @Override
    public AlphaColorModifier create(Level level, Biome biome, BlockPos pos) {
        Integer value = level.environmentAttributes().getValue(attribute, pos);
        return new AlphaColorModifier(this, Color4I.rgba(value), attribute, false);
    }

    @Override
    public Integer getAttributeValue(Color4I value) {
        return value.rgba();
    }

    @Override
    public EnvironmentAttribute<Integer> attribute() {
        return attribute;
    }
}
