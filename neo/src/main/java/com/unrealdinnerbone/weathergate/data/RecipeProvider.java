package com.unrealdinnerbone.weathergate.data;

import com.unrealdinnerbone.weathergate.WeatherGate;
import com.unrealdinnerbone.weathergate.WeatherGateRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;

import java.util.concurrent.CompletableFuture;

public class RecipeProvider extends net.minecraft.data.recipes.RecipeProvider {

    public RecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    protected void buildRecipes() {
        shaped(RecipeCategory.TRANSPORTATION, WeatherGateRegistry.SNOW_CATCHER.get())
                .pattern("SSS")
                .pattern("SCS")
                .pattern("SSS")
                .define('S', Blocks.SNOW_BLOCK)
                .define('C', Blocks.CAULDRON)
                .unlockedBy("has_snow", has(Blocks.SNOW_BLOCK))
                .save(this.output, ResourceKey.create(Registries.RECIPE, WeatherGate.id("snow_catcher")));

        shaped(RecipeCategory.DECORATIONS, WeatherGateRegistry.TERIANN_CONTROLLER.get())
                .pattern("OOO")
                .pattern("WBW")
                .pattern("GGG")
                .define('O', ItemTags.LEAVES)
                .define('W', Items.WATER_BUCKET)
                .define('B', Blocks.OAK_SAPLING)
                .define('G', Blocks.GRASS_BLOCK)
                .unlockedBy("has_sapling", has(Blocks.OAK_SAPLING))
                .save(this.output, ResourceKey.create(Registries.RECIPE, WeatherGate.id("teriann_controller")));
    }
}
