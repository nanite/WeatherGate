package com.unrealdinnerbone.weathergate.server;

import com.unrealdinnerbone.weathergate.WeatherGateRegistries;
import com.unrealdinnerbone.weathergate.level.attachments.TerrainControllerAttachment;
import com.unrealdinnerbone.weathergate.level.attachments.terrain.ControllerData;
import com.unrealdinnerbone.weathergate.modifers.types.TerrainModifierType;
import com.unrealdinnerbone.weathergate.modifers.base.TerrainModifier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

import java.util.HashMap;
import java.util.Map;

public class TerrainManager {

    public static void addBlockPos(Level level, BlockPos pos) {
        TerrainControllerAttachment attachment = TerrainControllerAttachment.getAttachment(level);
        Biome biome = level.getBiome(pos).value();

        Map<TerrainModifierType<?, ?>, TerrainModifier<?>> map = new HashMap<>();
        for (TerrainModifierType<?, ?> terrainModifier : WeatherGateRegistries.REGISTRY) {
            TerrainModifier<?> o = terrainModifier.create(level, biome, pos);
            map.put(terrainModifier, o);
        }

        attachment.setData(pos, new ControllerData(ControllerData.DEFAULT_RANGE, 1, map));
        attachment.save(level);
    }

    public static void removeBlockPos(Level level, BlockPos pos) {
        TerrainControllerAttachment attachment = TerrainControllerAttachment.getAttachment(level);
        attachment.removeData(pos);
        attachment.save(level);
    }
}
