package com.unrealdinnerbone.weathergate.modifers;

import com.unrealdinnerbone.weathergate.WeatherGate;
import com.unrealdinnerbone.weathergate.modifers.base.AbstactColorTerrainModifier;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

public class WaterColorTerrainModifier extends AbstactColorTerrainModifier {

    private static final Identifier ID = WeatherGate.id("water");

    @Override
    public Color4I getDefaultValue(Level level, Biome biome, BlockPos pos) {
        return Color4I.rgb(biome.getWaterColor());
    }


    @Override
    public Identifier id() {
        return ID;
    }

    @Override
    public boolean requireReRender() {
        return true;
    }
}
