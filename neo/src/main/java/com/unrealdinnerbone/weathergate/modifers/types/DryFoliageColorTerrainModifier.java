package com.unrealdinnerbone.weathergate.modifers.types;

import dev.ftb.mods.ftblibrary.icon.Color4I;

public class DryFoliageColorTerrainModifier extends BasicColorType {

    public DryFoliageColorTerrainModifier() {
        super((level, biome, blockPos) -> biome.getSpecialEffects().dryFoliageColorOverride()
                .map(Color4I::rgb)
                .orElse(Color4I.GREEN));
    }

    @Override
    public boolean requireReRender() {
        return true;
    }
}
