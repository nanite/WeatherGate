package com.unrealdinnerbone.weathergate.server;

import com.unrealdinnerbone.weathergate.level.attachments.TerrainControllerAttachment;
import com.unrealdinnerbone.weathergate.level.attachments.terrain.ControllerData;
import com.unrealdinnerbone.weathergate.level.attachments.terrain.ModifierState;
import com.unrealdinnerbone.weathergate.level.attachments.terrain.StoredData;
import com.unrealdinnerbone.weathergate.registry.TerrainModifiers;
import com.unrealdinnerbone.weathergate.modifers.base.TerrainModifier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TerrainManager {

    public static void addBlockPos(Level level, BlockPos pos) {
        TerrainControllerAttachment attachment = TerrainControllerAttachment.getAttachment(level);
        Biome biome = level.getBiome(pos).value();

        Map<TerrainModifier<?>, ModifierState<?>> colorMap = new HashMap<>();
        for (TerrainModifier<?> terrainModifier : TerrainModifiers.MODIFIERS_REGISTRY) {
            colorMap.put(
                    terrainModifier,
                    new ModifierState<>(
                            terrainModifier.isEnabledByDefault(),
                            terrainModifier.getDefaultValue(level, biome, pos)
                    )
            );
        }

        attachment.setData(pos, new ControllerData(ControllerData.DEFAULT_RANGE, new StoredData(colorMap)));
        attachment.save(level);
    }

    public static void removeBlockPos(Level level, BlockPos pos) {
        TerrainControllerAttachment attachment = TerrainControllerAttachment.getAttachment(level);
        attachment.removeData(pos);
        attachment.save(level);
    }
}
