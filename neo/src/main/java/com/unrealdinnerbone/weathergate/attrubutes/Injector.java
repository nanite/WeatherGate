package com.unrealdinnerbone.weathergate.attrubutes;

import com.unrealdinnerbone.weathergate.client.WeatherGateClient;

import com.unrealdinnerbone.weathergate.registry.TerrainModifiers;
import com.unrealdinnerbone.weathergate.modifers.base.TerrainModifier;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.attribute.EnvironmentAttributeLayer;
import net.minecraft.world.attribute.EnvironmentAttributeSystem;
import net.minecraft.world.attribute.EnvironmentAttributes;

public class Injector {
    public static EnvironmentAttributeSystem.Builder inject(ClientLevel level, EnvironmentAttributeSystem.Builder environmentAttributes) {
        return environmentAttributes
                .addPositionalLayer(EnvironmentAttributes.SKY_LIGHT_COLOR, ofType(level, TerrainModifiers.SKY_LIGHT_COLOR))
                .addPositionalLayer(EnvironmentAttributes.FOG_COLOR, ofType(level, TerrainModifiers.FOG_COLOR))
                .addPositionalLayer(EnvironmentAttributes.WATER_FOG_COLOR, ofType(level, TerrainModifiers.WATER_FOG_COLOR))
                .addPositionalLayer(EnvironmentAttributes.SKY_COLOR, ofType(level, TerrainModifiers.SKY_COLOR))
                .addPositionalLayer(EnvironmentAttributes.SUNRISE_SUNSET_COLOR, ofTypeA(level, TerrainModifiers.SUNRISE_SUNSET_COLOR))
                .addPositionalLayer(EnvironmentAttributes.CLOUD_COLOR, ofTypeA(level, TerrainModifiers.CLOUD_COLOR))
                .addPositionalLayer(EnvironmentAttributes.BLOCK_LIGHT_TINT, ofType(level, TerrainModifiers.BLOCK_LIGHT_TINT))
                .addPositionalLayer(EnvironmentAttributes.NIGHT_VISION_COLOR, ofType(level, TerrainModifiers.NIGHT_VISION_COLOR))
                .addPositionalLayer(EnvironmentAttributes.AMBIENT_LIGHT_COLOR, ofType(level, TerrainModifiers.AMBIENT_LIGHT_COLOR));
    }

    private static EnvironmentAttributeLayer.Positional<Integer> ofType(ClientLevel level, TerrainModifier<Color4I> modifier) {
        return (baseValue, pos, biomeInterpolator) -> WeatherGateClient.getDataForPosition(level, (int) pos.x(), (int) pos.z(), modifier).map(Color4I::rgb).orElse(baseValue);
    }

    private static EnvironmentAttributeLayer.Positional<Integer> ofTypeA(ClientLevel level, TerrainModifier<Color4I> modifier) {
        return (baseValue, pos, biomeInterpolator) -> WeatherGateClient.getDataForPosition(level, (int) pos.x(), (int) pos.z(), modifier).map(Color4I::rgba).orElse(baseValue);
    }
}
