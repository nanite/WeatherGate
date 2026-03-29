package com.unrealdinnerbone.weathergate.modifers;

import com.unrealdinnerbone.weathergate.WeatherGate;
import com.unrealdinnerbone.weathergate.modifers.base.AbstactColorTerrainModifier;
import com.unrealdinnerbone.weathergate.modifers.base.EnvironmentAttributeModifier;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

public class ColorModifier extends AbstactColorTerrainModifier implements EnvironmentAttributeModifier<Color4I, Integer> {

    protected final EnvironmentAttribute<Integer> attribute;
    private final Identifier id;

    ColorModifier(EnvironmentAttribute<Integer> attribute, Identifier identifier) {
        this.attribute = attribute;
        this.id = identifier;
    }

    public static ColorModifier of(String id, EnvironmentAttribute<Integer> attribute) {
        return new ColorModifier(attribute, WeatherGate.id(id));
    }

    @Override
    public Color4I getDefaultValue(Level level, Biome biome, BlockPos pos) {
        return Color4I.rgb(level.environmentAttributes().getValue(this.attribute, pos));
    }

    @Override
    public Identifier id() {
        return id;
    }

    @Override
    public Integer getAttributeValue(Color4I value) {
        return value.rgb();
    }

    @Override
    public EnvironmentAttribute<Integer> attribute() {
        return attribute;
    }
}
