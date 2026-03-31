package com.unrealdinnerbone.weathergate.modifers.types;

import dev.ftb.mods.ftblibrary.icon.Color4I;

public class GrassColorTerrainModifier extends BasicColorType {

    public GrassColorTerrainModifier() {
        super((level, biome, blockPos) -> biome.getSpecialEffects().grassColorOverride()
                .map(Color4I::rgb)
                .orElse(Color4I.GREEN));
    }

    @Override
    public boolean requireReRender() {
        return true;
    }
}
