package com.unrealdinnerbone.weathergate.server;

import com.unrealdinnerbone.weathergate.level.attachments.TerrainControllerAttachment;
import com.unrealdinnerbone.weathergate.registry.TerrainModifiers;
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

        Map<TerrainModifier<?>, TerrainControllerAttachment.ModifierState<?>> colorMap = new HashMap<>();
        for (TerrainModifier<?> terrainModifier : TerrainModifiers.MODIFIERS_REGISTRY) {
            colorMap.put(
                    terrainModifier,
                    new TerrainControllerAttachment.ModifierState<>(
                            terrainModifier.isEnabledByDefault(),
                            terrainModifier.getDefaultValue(level, biome, pos)
                    )
            );
        }

        attachment.data().put(pos, new TerrainControllerAttachment.StoredData(colorMap));
        attachment.save(level);
    }

    public static void removeBlockPos(Level level, BlockPos pos) {
        TerrainControllerAttachment attachment = TerrainControllerAttachment.getAttachment(level);
        attachment.data().remove(pos);
        attachment.save(level);
    }
}
