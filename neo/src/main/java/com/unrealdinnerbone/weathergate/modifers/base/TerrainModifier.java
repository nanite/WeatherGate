package com.unrealdinnerbone.weathergate.modifers.base;

import com.mojang.serialization.Codec;
import com.unrealdinnerbone.weathergate.client.screen.TerrainControllerScreen2;
import com.unrealdinnerbone.weathergate.level.attachments.TerrainControllerAttachment;
import dev.ftb.mods.ftblibrary.client.gui.widget.Panel;
import dev.ftb.mods.ftblibrary.client.gui.widget.SimpleButton;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

public interface TerrainModifier<T> {

    T getDefaultValue(Level level, Biome biome, BlockPos pos);

    default boolean isEnabledByDefault() {
        return false;
    }

    default boolean requireReRender() {
        return false;
    }

    Identifier id();

    SimpleButton createButton(
            Panel basePanel,
            TerrainControllerScreen2 screen2,
            Panel panel,
            BlockPos blockPos,
            TerrainControllerAttachment.ModifierState<T> state
    );

    StreamCodec<RegistryFriendlyByteBuf, T> getStreamCodec();

    Codec<T> getCodec();
}