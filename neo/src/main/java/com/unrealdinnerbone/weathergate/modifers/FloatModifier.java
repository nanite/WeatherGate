package com.unrealdinnerbone.weathergate.modifers;

import com.unrealdinnerbone.weathergate.WeatherGate;
import com.unrealdinnerbone.weathergate.modifers.base.AbstactFloatTerrainModifier;
import com.unrealdinnerbone.weathergate.modifers.base.EnvironmentAttributeModifier;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

public class FloatModifier extends AbstactFloatTerrainModifier implements EnvironmentAttributeModifier<Float, Float> {

    protected final EnvironmentAttribute<Float> attribute;
    private final Identifier id;
    private final float minValue;
    private final float maxValue;

    FloatModifier(EnvironmentAttribute<Float> attribute, Identifier identifier, float minValue, float maxValue) {
        this.attribute = attribute;
        this.id = identifier;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public static FloatModifier of(String id, EnvironmentAttribute<Float> attribute, float minValue, float maxValue) {
        return new FloatModifier(attribute, WeatherGate.id(id), minValue, maxValue);
    }

    public static FloatModifier of(String id, EnvironmentAttribute<Float> attribute) {
        return of(id, attribute, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY);
    }

    public static FloatModifier nonNegative(String id, EnvironmentAttribute<Float> attribute) {
        return of(id, attribute, 0, Float.POSITIVE_INFINITY);
    }

    @Override
    public float getMinValue() {
        return minValue;
    }

    @Override
    public float getMaxValue() {
        return maxValue;
    }

    @Override
    public Float getDefaultValue(Level level, Biome biome, BlockPos pos) {
        return level.environmentAttributes().getValue(this.attribute, pos);
    }

    @Override
    public Identifier id() {
        return id;
    }

    @Override
    public Float getAttributeValue(Float value) {
        return value;
    }

    @Override
    public EnvironmentAttribute<Float> attribute() {
        return attribute;
    }
}
