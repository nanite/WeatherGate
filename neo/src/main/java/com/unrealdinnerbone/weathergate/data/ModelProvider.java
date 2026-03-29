package com.unrealdinnerbone.weathergate.data;

import com.unrealdinnerbone.weathergate.WeatherGate;
import com.unrealdinnerbone.weathergate.WeatherGateRegistry;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.data.PackOutput;

public class ModelProvider extends net.minecraft.client.data.models.ModelProvider {

    public ModelProvider(PackOutput output) {
        super(output, WeatherGate.MOD_ID);
    }


    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        blockModels.createTrivialCube(WeatherGateRegistry.SNOW_CATCHER.get());
        blockModels.createTrivialCube(WeatherGateRegistry.TERIANN_CONTROLLER.get());

//        itemModels.
    }

//    @Override
//    protected void registerStatesAndModels() {
//        standardBlock(WeatherGateRegistry.SNOW_CATCHER.get());
//        standardBlock(WeatherGateRegistry.TERIANN_CONTROLLER.get());
//        standardBlock(WeatherGateRegistry.SUN_IN_A_BOX.get());
}
//
//    private void standardBlock(Block block) {
//        simpleBlockWithItem(block, cubeAll(block));
//    }
