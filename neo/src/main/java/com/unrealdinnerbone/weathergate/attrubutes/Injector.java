package com.unrealdinnerbone.weathergate.attrubutes;

import com.unrealdinnerbone.weathergate.WeatherGateRegistries;
import com.unrealdinnerbone.weathergate.client.WeatherGateClient;

import com.unrealdinnerbone.weathergate.modifers.base.EnvironmentAttributeModifier;
import com.unrealdinnerbone.weathergate.modifers.types.TerrainModifierType;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.attribute.EnvironmentAttributeLayer;
import net.minecraft.world.attribute.EnvironmentAttributeSystem;

public class Injector {
    public static EnvironmentAttributeSystem.Builder inject(ClientLevel level, EnvironmentAttributeSystem.Builder environmentAttributes) {
        for (TerrainModifierType<?, ?> terrainModifier : WeatherGateRegistries.REGISTRY) {
            environmentAttributes = addLayer(level, environmentAttributes, terrainModifier);
        }
        return environmentAttributes;
    }

    private static EnvironmentAttributeSystem.Builder addLayer(ClientLevel level, EnvironmentAttributeSystem.Builder builder, TerrainModifierType<?, ?> terrainModifier) {
        if (terrainModifier instanceof EnvironmentAttributeModifier<?, ?> attributeModifier) {
            return addLayerTyped(level, builder, terrainModifier, attributeModifier);
        }
        return builder;
    }

    @SuppressWarnings("unchecked")
    private static <O, M> EnvironmentAttributeSystem.Builder addLayerTyped(ClientLevel level, EnvironmentAttributeSystem.Builder builder, TerrainModifierType<?, ?> terrainModifier, EnvironmentAttributeModifier<?, ?> attributeModifier) {
        TerrainModifierType<O, ?> typedType = (TerrainModifierType<O, ?>) terrainModifier;
        EnvironmentAttributeModifier<O, M> typedAttribute = (EnvironmentAttributeModifier<O, M>) attributeModifier;
        return builder.addPositionalLayer(typedAttribute.attribute(), ofType(level, typedType, typedAttribute));
    }

    private static <O, M> EnvironmentAttributeLayer.Positional<M> ofType(ClientLevel level, TerrainModifierType<O, ?> type, EnvironmentAttributeModifier<O, M> attributeModifier) {
        return (baseValue, pos, biomeInterpolator) ->
                WeatherGateClient.getDataForPosition(level, (int) pos.x(), (int) pos.z(), type)
                        .map(attributeModifier::getAttributeValue)
                        .orElse(baseValue);
    }
}
