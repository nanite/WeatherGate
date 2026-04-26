package com.unrealdinnerbone.weathergate.client;

import com.unrealdinnerbone.weathergate.client.compact.SereneSeasonsCompact;
import com.unrealdinnerbone.weathergate.level.attachments.TerrainControllerAttachment;
import com.unrealdinnerbone.weathergate.level.attachments.terrain.ControllerData;
import com.unrealdinnerbone.weathergate.modifers.types.TerrainModifierType;
import com.unrealdinnerbone.weathergate.registry.ModifierTypes;
import com.unrealdinnerbone.weathergate.util.RangeUtils;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.Map;
import java.util.Optional;

public class WeatherGateClient
{

    public static void init(IEventBus eventBus) {
        eventBus.addListener(EventPriority.LOWEST, WeatherGateClient::onClientSetup);
    }

    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            if(ModList.get().isLoaded("sereneseasons")) {
                SereneSeasonsCompact.registerCompact();
                WeatherGateClient.injectResolvers(false);
            }
            WeatherGateClient.injectResolvers(true);
        });
    }

    public static void injectResolvers(boolean basic) {
        if(basic) {
            BiomeColors.GRASS_COLOR_RESOLVER = createForType(BiomeColors.GRASS_COLOR_RESOLVER, ModifierTypes.GRASS.get());
            BiomeColors.FOLIAGE_COLOR_RESOLVER = createForType(BiomeColors.FOLIAGE_COLOR_RESOLVER, ModifierTypes.FOLIAGE.get());
        }
        BiomeColors.WATER_COLOR_RESOLVER = createForType(BiomeColors.WATER_COLOR_RESOLVER, ModifierTypes.WATER.get());
        BiomeColors.DRY_FOLIAGE_COLOR_RESOLVER = createForType(BiomeColors.DRY_FOLIAGE_COLOR_RESOLVER, ModifierTypes.DRY_FOLIAGE.get());
    }


    public static ColorResolver createForType(ColorResolver minecraftResolver, TerrainModifierType<Color4I, ?> type) {
        return (biome, x, z) -> getColorAtLocation((int) x, (int) z, type).map(Color4I::rgb).orElse(minecraftResolver.getColor(biome, x, z));
    }

    public static Optional<Color4I> getColorAtLocation(int x, int z, TerrainModifierType<Color4I, ?> type) {
        Minecraft instance = Minecraft.getInstance();
        if (instance.level == null) {
            return Optional.empty();
        }
        return getDataForPosition(Minecraft.getInstance().level, x, z, type);
    }

    public static <T> Optional<T> getDataForPosition(Level level, int x, int z, TerrainModifierType<T, ?> type) {
        TerrainControllerAttachment terrainControllerAttachment = TerrainControllerAttachment.getAttachment(level);
        if(terrainControllerAttachment != null) {
            T bestValue = null;
            int highestPriority = -1;

            for (Map.Entry<BlockPos, ControllerData> blockPosMapEntry : terrainControllerAttachment.entrySet()) {
                BlockPos key = blockPosMapEntry.getKey();
                ControllerData data = blockPosMapEntry.getValue();

                if (RangeUtils.isWithinRange(key.getX(), key.getZ(), x, z, data.getRange())) {
                    T modifierValue = terrainControllerAttachment.getModifierValue(key, type);

                    if (modifierValue != null && data.getPriory() > highestPriority) {
                        bestValue = modifierValue;
                        highestPriority = data.getPriory();
                    }
                }
            }

            return Optional.ofNullable(bestValue);
        }
        return Optional.empty();
    }
}
