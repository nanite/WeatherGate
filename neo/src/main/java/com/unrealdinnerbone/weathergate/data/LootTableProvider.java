package com.unrealdinnerbone.weathergate.data;

import com.unrealdinnerbone.weathergate.WeatherGate;
import net.minecraft.core.HolderLookup;
import com.unrealdinnerbone.weathergate.WeatherGateRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

public class LootTableProvider extends BlockLootSubProvider {

    protected LootTableProvider(HolderLookup.Provider provider) {
        super(Collections.emptySet(), FeatureFlags.REGISTRY.allFlags(), provider);
    }

    @Override
    protected void generate() {
        dropSelf(WeatherGateRegistry.SNOW_CATCHER.get());
        dropSelf(WeatherGateRegistry.TERIANN_CONTROLLER.get());
    }

    @Override
    @NotNull
    protected Iterable<Block> getKnownBlocks() {
        return BuiltInRegistries.BLOCK.stream().toList().stream()
                .filter(block -> BuiltInRegistries.BLOCK.getKey(block).getNamespace().equals(WeatherGate.MOD_ID)).toList();
    }
}
