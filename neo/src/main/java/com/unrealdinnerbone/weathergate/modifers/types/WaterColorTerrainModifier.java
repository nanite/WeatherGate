package com.unrealdinnerbone.weathergate.modifers.types;

import dev.ftb.mods.ftblibrary.icon.Color4I;

public class WaterColorTerrainModifier extends BasicColorType {

    public WaterColorTerrainModifier() {
        super((level, biome, blockPos) -> Color4I.rgb(biome.getSpecialEffects().waterColor()));
    }

    @Override
    public boolean requireReRender() {
        return true;
    }
}
