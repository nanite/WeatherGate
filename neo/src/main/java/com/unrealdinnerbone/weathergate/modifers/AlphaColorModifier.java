package com.unrealdinnerbone.weathergate.modifers;

import com.mojang.serialization.Codec;
import com.unrealdinnerbone.weathergate.WeatherGate;
import com.unrealdinnerbone.weathergate.WeatherGateCodecs;
import com.unrealdinnerbone.weathergate.modifers.base.AbstactColorTerrainModifier;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

public class AlphaColorModifier extends ColorModifier {

    private AlphaColorModifier(EnvironmentAttribute<Integer> attribute, Identifier identifier) {
        super(attribute, identifier);
    }

    @Override
    public Codec<Color4I> getCodec() {
        return WeatherGateCodecs.COLOR4I_ALPAH_CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, Color4I> getStreamCodec() {
        return WeatherGateCodecs.COLOR4I_ALPHA_STREAM_CODEC;
    }

    public static AlphaColorModifier of(String id, EnvironmentAttribute<Integer> attribute) {
        return new AlphaColorModifier(attribute, WeatherGate.id(id));
    }

    @Override
    public boolean supportsAlpha() {
        return true;
    }

    @Override
    public Color4I getDefaultValue(Level level, Biome biome, BlockPos pos) {
        return Color4I.rgba(level.environmentAttributes().getValue(this.attribute, pos));
    }

    @Override
    public Integer getAttributeValue(Color4I value) {
        return value.rgba();
    }
}
