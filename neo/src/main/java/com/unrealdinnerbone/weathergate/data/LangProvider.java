package com.unrealdinnerbone.weathergate.data;

import com.unrealdinnerbone.weathergate.WeatherGate;
import com.unrealdinnerbone.weathergate.WeatherGateRegistry;
import com.unrealdinnerbone.weathergate.modifers.base.TerrainModifier;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.registries.DeferredHolder;

public class LangProvider extends LanguageProvider {

    public LangProvider(PackOutput output) {
        super(output, WeatherGate.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add(WeatherGateRegistry.SNOW_CATCHER.get(), "Snow Catcher");
        add(WeatherGateRegistry.TERIANN_CONTROLLER.get(), "Teriann Controller");
        add(WeatherGateRegistry.TERIANN_CONTROLLER_ITEM.get(), "Teriann Controller");
        add(WeatherGateRegistry.SNOW_CATCHER_ITEM.get(), "Snow Catcher");
        add("block.weathergate.terrain_controller.no_data", "No Data Found! Replace Block and Try Again!");
        add("weathergate.sun_in_a_box.tooltip", "Make Sun Light value always noon (Client Only)");
//        add(TerrainModifiers.GRASS, "Grass Color", "Overrides the biome grass tint color.");
//        add(TerrainModifiers.FOLIAGE, "Foliage Color", "Overrides the biome foliage tint color.");
//        add(TerrainModifiers.WATER, "Water Color", "Overrides the biome water tint color.");
//        add(TerrainModifiers.DRY_FOLIAGE, "Dry Foliage Color", "Overrides the biome dry foliage tint color.");
//
//        add(TerrainModifiers.FOG_COLOR, "Fog Color", "Sets the world fog color.");
//        add(TerrainModifiers.WATER_FOG_COLOR, "Water Fog Color", "Sets the underwater fog color.");
//        add(TerrainModifiers.SKY_COLOR, "Sky Color", "Sets the sky color.");
//        add(TerrainModifiers.SUNRISE_SUNSET_COLOR, "Sunrise/Sunset Color", "Sets the sunrise and sunset horizon tint (includes alpha).");
//        add(TerrainModifiers.CLOUD_COLOR, "Cloud Color", "Sets the cloud color (includes alpha).");
//        add(TerrainModifiers.BLOCK_LIGHT_TINT, "Block Light Tint", "Tints light emitted from blocks.");
//        add(TerrainModifiers.SKY_LIGHT_COLOR, "Sky Light Color", "Sets the color of skylight.");
//        add(TerrainModifiers.NIGHT_VISION_COLOR, "Night Vision Color", "Sets the color tint used by night vision.");
//        add(TerrainModifiers.AMBIENT_LIGHT_COLOR, "Ambient Light Color", "Sets the ambient light color.");
//
//        add(TerrainModifiers.FOG_START_DISTANCE, "Fog Start Distance", "Distance from camera where fog begins.");
//        add(TerrainModifiers.FOG_END_DISTANCE, "Fog End Distance", "Distance from camera where fog reaches full intensity.");
//        add(TerrainModifiers.SKY_FOG_END_DISTANCE, "Sky Fog End Distance", "Distance where sky fog reaches full intensity.");
//        add(TerrainModifiers.CLOUD_FOG_END_DISTANCE, "Cloud Fog End Distance", "Distance where cloud fog reaches full intensity.");
//        add(TerrainModifiers.WATER_FOG_START_DISTANCE, "Water Fog Start Distance", "Distance from camera where underwater fog begins.");
//        add(TerrainModifiers.WATER_FOG_END_DISTANCE, "Water Fog End Distance", "Distance from camera where underwater fog reaches full intensity.");
//        add(TerrainModifiers.CLOUD_HEIGHT, "Cloud Height", "Sets the rendered cloud layer height.");
//        add(TerrainModifiers.STAR_BRIGHTNESS, "Star Brightness", "Controls star brightness in the night sky.");
//        add(TerrainModifiers.SKY_LIGHT_FACTOR, "Sky Light Factor", "Scales sky light contribution (0 = none, 1 = normal).");
//
//        add(TerrainModifiers.SUN_ANGLE, "Sun Angle", "Sets the sun rotation angle in degrees (0-360).");
//        add(TerrainModifiers.MOON_ANGLE, "Moon Angle", "Sets the moon rotation angle in degrees (0-360).");
//        add(TerrainModifiers.STAR_ANGLE, "Star Angle", "Sets the star field rotation angle in degrees (0-360).");

    }

    private void add(DeferredHolder<TerrainModifier<?>, ?> holder, String name, String description) {
        add(holder.getId().toLanguageKey(), name);
        add(holder.getId().toLanguageKey() + ".info", description);
    }
}
