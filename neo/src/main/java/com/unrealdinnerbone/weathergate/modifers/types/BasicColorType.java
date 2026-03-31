package com.unrealdinnerbone.weathergate.modifers.types;

import com.mojang.serialization.MapCodec;
import com.unrealdinnerbone.weathergate.modifers.BasicColorModifier;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import org.apache.commons.lang3.function.TriFunction;

public class BasicColorType extends TerrainModifierType<Color4I, BasicColorModifier> {

    private final MapCodec<BasicColorModifier> codec;
    private final StreamCodec<? super RegistryFriendlyByteBuf, BasicColorModifier> streamCodec;
    private final TriFunction<Level, Biome, BlockPos, Color4I> factory;

    protected BasicColorType(TriFunction<Level, Biome, BlockPos, Color4I> factory) {
        this.factory = factory;
        this.codec = createCodec();
        this.streamCodec = createStreamCodec();
    }

    private MapCodec<BasicColorModifier> createCodec() {
        return BasicColorModifier.Data.CODEC.xmap(data -> new BasicColorModifier(this, data.value(), data.enabled()), BasicColorModifier::data).fieldOf("value");
    }

    private StreamCodec<? super ByteBuf, BasicColorModifier> createStreamCodec() {
        return BasicColorModifier.Data.STREAM_CODEC.map(data -> new BasicColorModifier(this, data.value(), data.enabled()),  BasicColorModifier::data);
    }

    @Override
    public MapCodec<BasicColorModifier> codec() {
        return codec;
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, BasicColorModifier> streamCodec() {
        return streamCodec;
    }

    @Override
    public BasicColorModifier create(Level level, Biome biome, BlockPos pos) {
        Color4I apply = factory.apply(level, biome, pos);
        return new BasicColorModifier(this, apply, false);
    }
}
