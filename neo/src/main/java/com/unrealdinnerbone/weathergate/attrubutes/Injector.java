package com.unrealdinnerbone.weathergate.attrubutes;

import com.unrealdinnerbone.weathergate.client.WeatherGateClient;

import com.unrealdinnerbone.weathergate.modifers.base.EnvironmentAttributeModifier;
import com.unrealdinnerbone.weathergate.registry.TerrainModifiers;
import com.unrealdinnerbone.weathergate.modifers.base.TerrainModifier;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.attribute.EnvironmentAttributeLayer;
import net.minecraft.world.attribute.EnvironmentAttributeSystem;

public class Injector {
    public static EnvironmentAttributeSystem.Builder inject(ClientLevel level, EnvironmentAttributeSystem.Builder environmentAttributes) {
        for (TerrainModifier<?> terrainModifier : TerrainModifiers.MODIFIERS_REGISTRY) {
            environmentAttributes = addLayer(level, environmentAttributes, terrainModifier);
        }
//        return environmentAttributes
//                .addPositionalLayer(EnvironmentAttributes.SKY_LIGHT_COLOR, ofType(level, TerrainModifiers.SKY_LIGHT_COLOR))
//                .addPositionalLayer(EnvironmentAttributes.FOG_COLOR, ofType(level, TerrainModifiers.FOG_COLOR))
//                .addPositionalLayer(EnvironmentAttributes.WATER_FOG_COLOR, ofType(level, TerrainModifiers.WATER_FOG_COLOR))
//                .addPositionalLayer(EnvironmentAttributes.SKY_COLOR, ofType(level, TerrainModifiers.SKY_COLOR))
//                .addPositionalLayer(EnvironmentAttributes.SUNRISE_SUNSET_COLOR, ofType(level, TerrainModifiers.SUNRISE_SUNSET_COLOR))
//                .addPositionalLayer(EnvironmentAttributes.CLOUD_COLOR, ofType(level, TerrainModifiers.CLOUD_COLOR))
//                .addPositionalLayer(EnvironmentAttributes.BLOCK_LIGHT_TINT, ofType(level, TerrainModifiers.BLOCK_LIGHT_TINT))
//                .addPositionalLayer(EnvironmentAttributes.NIGHT_VISION_COLOR, ofType(level, TerrainModifiers.NIGHT_VISION_COLOR))
//                .addPositionalLayer(EnvironmentAttributes.AMBIENT_LIGHT_COLOR, ofType(level, TerrainModifiers.AMBIENT_LIGHT_COLOR));
        return environmentAttributes;
    }

    private static EnvironmentAttributeSystem.Builder addLayer(ClientLevel level, EnvironmentAttributeSystem.Builder builder, TerrainModifier<?> terrainModifier) {
        if (terrainModifier instanceof EnvironmentAttributeModifier<?, ?> attributeModifier) {
            return addLayerTyped(level, builder, terrainModifier, attributeModifier);
        }
        return builder;
    }

    @SuppressWarnings("unchecked")
    private static <O, M> EnvironmentAttributeSystem.Builder addLayerTyped(ClientLevel level, EnvironmentAttributeSystem.Builder builder, TerrainModifier<?> terrainModifier, EnvironmentAttributeModifier<?, ?> attributeModifier) {
        TerrainModifier<O> typedTerrain = (TerrainModifier<O>) terrainModifier;
        EnvironmentAttributeModifier<O, M> typedAttribute = (EnvironmentAttributeModifier<O, M>) attributeModifier;
        return builder.addPositionalLayer(typedAttribute.attribute(), ofType(level, typedTerrain, typedAttribute));
    }

    private static <O, M> EnvironmentAttributeLayer.Positional<M> ofType(ClientLevel level, TerrainModifier<O> terrainModifier, EnvironmentAttributeModifier<O, M> attributeModifier) {
        return (baseValue, pos, biomeInterpolator) ->
                WeatherGateClient.getDataForPosition(level, (int) pos.x(), (int) pos.z(), terrainModifier)
                        .map(attributeModifier::getAttributeValue)
                        .orElse(baseValue);
    }
}
