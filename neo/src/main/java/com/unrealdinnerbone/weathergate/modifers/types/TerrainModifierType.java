package com.unrealdinnerbone.weathergate.modifers.types;

import com.mojang.serialization.MapCodec;
import com.unrealdinnerbone.weathergate.modifers.base.TerrainModifier;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

public abstract class TerrainModifierType<B, T extends TerrainModifier<B>> {

    public abstract MapCodec<T> codec();

    public abstract StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec();

    public abstract T create(Level level, Biome biome, BlockPos pos);

    public boolean requireReRender() {
        return false;
    }
}
