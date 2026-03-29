package com.unrealdinnerbone.weathergate.modifers.base;

import com.mojang.serialization.Codec;
import dev.ftb.mods.ftblibrary.client.gui.widget.ModalPanel;
import dev.ftb.mods.ftblibrary.client.gui.widget.Panel;
import dev.ftb.mods.ftblibrary.icon.Icon;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

import java.util.function.Consumer;

public interface TerrainModifier<T> {

    T getDefaultValue(Level level, Biome biome, BlockPos pos);

    default boolean isEnabledByDefault() {
        return false;
    }

    default boolean requireReRender() {
        return false;
    }

    Identifier id();

    StreamCodec<RegistryFriendlyByteBuf, T> getStreamCodec();

    Codec<T> getCodec();

    Icon<?> getIcon(T value);

    ModalPanel createEditPanel(Panel basePanel, T activeValue, Consumer<T> newValueApplier);
}