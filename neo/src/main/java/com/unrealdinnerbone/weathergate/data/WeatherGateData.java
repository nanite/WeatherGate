package com.unrealdinnerbone.weathergate.data;

import com.unrealdinnerbone.weathergate.WeatherGate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.neoforge.common.data.internal.NeoForgeRecipeProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class WeatherGateData {

    public static void onData(GatherDataEvent.Client event) {
        event.createProvider(TagProvider.Blocks::new);
        event.createProvider(LangProvider::new);
        event.createProvider(WeatherGateData::create);
        event.createProvider(Runner::new);
        event.createProvider(ModelProvider::new);
    }

    public static net.minecraft.data.loot.LootTableProvider create(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        return new net.minecraft.data.loot.LootTableProvider(
                pOutput,
                Collections.emptySet(),
                List.of(
                        new net.minecraft.data.loot.LootTableProvider.SubProviderEntry(LootTableProvider::new, LootContextParamSets.BLOCK)
                ),
                lookupProvider
        );
    }

    public static class Runner extends net.minecraft.data.recipes.RecipeProvider.Runner {
        public Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
            super(output, lookupProvider);
        }

        @Override
        protected net.minecraft.data.recipes.RecipeProvider createRecipeProvider(HolderLookup.Provider lookupProvider, RecipeOutput output) {
            return new RecipeProvider(lookupProvider, output);
        }

        @Override
        public String getName() {
            return "NeoForge recipes";
        }
    }

}
