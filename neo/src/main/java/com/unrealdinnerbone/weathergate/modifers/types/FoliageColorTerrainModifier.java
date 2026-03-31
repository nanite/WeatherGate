package com.unrealdinnerbone.weathergate.modifers.types;

import dev.ftb.mods.ftblibrary.icon.Color4I;

public class FoliageColorTerrainModifier extends BasicColorType {

    public FoliageColorTerrainModifier() {
        super((level, biome, blockPos) -> biome.getSpecialEffects().foliageColorOverride()
                .map(Color4I::rgb)
                .orElse(Color4I.GREEN));
    }

    @Override
    public boolean requireReRender() {
        return true;
    }
}
