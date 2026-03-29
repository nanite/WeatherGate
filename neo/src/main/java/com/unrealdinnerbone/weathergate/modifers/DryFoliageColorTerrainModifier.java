package com.unrealdinnerbone.weathergate.modifers;

import com.unrealdinnerbone.weathergate.WeatherGate;
import com.unrealdinnerbone.weathergate.modifers.base.AbstactColorTerrainModifier;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

public class DryFoliageColorTerrainModifier extends AbstactColorTerrainModifier {

    private static final Identifier ID = WeatherGate.id("dry_foliage");

    @Override
    public Color4I getDefaultValue(Level level, Biome biome, BlockPos pos) {
        return biome.getSpecialEffects().dryFoliageColorOverride()
                .map(Color4I::rgb)
                .orElse(Color4I.GREEN);
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
