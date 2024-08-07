package com.unrealdinnerbone.weathergate.data;

import com.unrealdinnerbone.weathergate.WeatherGate;
import com.unrealdinnerbone.weathergate.WeatherGateRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;

import java.util.concurrent.CompletableFuture;

public class RecipeProvider extends net.minecraft.data.recipes.RecipeProvider {

    public RecipeProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> providerCompletableFuture) {
        super(pOutput, providerCompletableFuture);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, WeatherGateRegistry.SNOW_CATCHER.get())
                .pattern("SSS")
                .pattern("SCS")
                .pattern("SSS")
                .define('S', Blocks.SNOW_BLOCK)
                .define('C', Blocks.CAULDRON)
                .unlockedBy("has_snow", has(Blocks.SNOW_BLOCK))
                .save(recipeOutput, WeatherGate.rl("snow_catcher"));

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, WeatherGateRegistry.TERIANN_CONTROLLER.get())
                .pattern("OOO")
                .pattern("WBW")
                .pattern("GGG")
                .define('O', ItemTags.LEAVES)
                .define('W', Items.WATER_BUCKET)
                .define('B', Blocks.OAK_SAPLING)
                .define('G', Blocks.GRASS_BLOCK)
                .unlockedBy("has_sapling", has(Blocks.OAK_SAPLING))
                .save(recipeOutput, WeatherGate.rl("teriann_controller"));


        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, WeatherGateRegistry.SUN_IN_A_BOX.get())
                .pattern("YYY")
                .pattern("YCY")
                .pattern("YYY")
                .define('C', Blocks.YELLOW_CONCRETE)
                .define('Y', Tags.Items.DYES_YELLOW)
                .unlockedBy("has_yellow", has(Tags.Items.DYES_YELLOW))
                .save(recipeOutput, WeatherGate.rl("sun_in_a_box"));

    }
}
