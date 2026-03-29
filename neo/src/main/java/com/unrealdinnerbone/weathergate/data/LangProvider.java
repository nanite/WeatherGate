package com.unrealdinnerbone.weathergate.data;

import com.unrealdinnerbone.weathergate.WeatherGate;
//import com.unrealdinnerbone.weathergate.util.Type;
import com.unrealdinnerbone.weathergate.WeatherGateRegistry;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class LangProvider extends LanguageProvider {

    public LangProvider(PackOutput output) {
        super(output, WeatherGate.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add(WeatherGateRegistry.SNOW_CATCHER.get(), "Snow Catcher");
        add(WeatherGateRegistry.TERIANN_CONTROLLER.get(), "Teriann Controller");
        add("block.weathergate.terrain_controller.no_data", "No Data Found! Replace Block and Try Again!");
        add("weathergate.sun_in_a_box.tooltip", "Make Sun Light value always noon (Client Only)");
//        add(Type.GRASS.getLangKey(), "Grass");
//        add(Type.FOLIAGE.getLangKey(), "Foliage");
//        add(Type.WATER.getLangKey(), "Water");
    }
}
