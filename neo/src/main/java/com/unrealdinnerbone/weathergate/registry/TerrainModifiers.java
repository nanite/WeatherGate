package com.unrealdinnerbone.weathergate.registry;

import com.unrealdinnerbone.weathergate.WeatherGate;
import com.unrealdinnerbone.weathergate.modifers.AlphaColorModifier;
import com.unrealdinnerbone.weathergate.modifers.ColorModifier;
import com.unrealdinnerbone.weathergate.modifers.DryFoliageColorTerrainModifier;
import com.unrealdinnerbone.weathergate.modifers.FloatModifier;
import com.unrealdinnerbone.weathergate.modifers.FoliageColorTerrainModifier;
import com.unrealdinnerbone.weathergate.modifers.GrassColorTerrainModifier;
import com.unrealdinnerbone.weathergate.modifers.WaterColorTerrainModifier;
import com.unrealdinnerbone.weathergate.modifers.base.AbstactFloatTerrainModifier;
import com.unrealdinnerbone.weathergate.modifers.base.TerrainModifier;
import com.unrealdinnerbone.weathergate.util.CodecRegistry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.attribute.EnvironmentAttributes;

public final class TerrainModifiers {
    private TerrainModifiers() {}

    public static final CodecRegistry<Identifier, TerrainModifier<?>> MODIFIERS_REGISTRY = CodecRegistry.identifierKeys();

    public static final StreamCodec<RegistryFriendlyByteBuf, TerrainModifier<?>> STREAM_CODEC = StreamCodec.of(
            (buf, modifier) -> {
                Identifier id = MODIFIERS_REGISTRY.getKey(modifier);
                if (id == null) throw new IllegalArgumentException("Unknown terrain modifier: " + modifier.id());
                Identifier.STREAM_CODEC.encode(buf, id);
            },
            buf -> MODIFIERS_REGISTRY.get(Identifier.STREAM_CODEC.decode(buf))
    );

    public static final GrassColorTerrainModifier GRASS = register(new GrassColorTerrainModifier());
    public static final FoliageColorTerrainModifier FOLIAGE = register(new FoliageColorTerrainModifier());
    public static final WaterColorTerrainModifier WATER = register(new WaterColorTerrainModifier());
    public static final DryFoliageColorTerrainModifier DRY_FOLIAGE = register(new DryFoliageColorTerrainModifier());
    public static final ColorModifier FOG_COLOR = register(ColorModifier.of("fog_color", EnvironmentAttributes.FOG_COLOR));
    public static final ColorModifier WATER_FOG_COLOR = register(ColorModifier.of("water_fog_color", EnvironmentAttributes.WATER_FOG_COLOR));
    public static final ColorModifier SKY_COLOR = register(ColorModifier.of("sky_color", EnvironmentAttributes.SKY_COLOR));
    public static final AlphaColorModifier SUNRISE_SUNSET_COLOR = register(AlphaColorModifier.of("sunrise_sunset_color", EnvironmentAttributes.SUNRISE_SUNSET_COLOR));
    public static final AlphaColorModifier CLOUD_COLOR = register(AlphaColorModifier.of("cloud_color", EnvironmentAttributes.CLOUD_COLOR));
    public static final ColorModifier BLOCK_LIGHT_TINT = register(ColorModifier.of("block_light_tint", EnvironmentAttributes.BLOCK_LIGHT_TINT));
    public static final ColorModifier SKY_LIGHT_COLOR = register(ColorModifier.of("sky_light_color", EnvironmentAttributes.SKY_LIGHT_COLOR));
    public static final ColorModifier NIGHT_VISION_COLOR = register(ColorModifier.of("night_vision_color", EnvironmentAttributes.NIGHT_VISION_COLOR));
    public static final ColorModifier AMBIENT_LIGHT_COLOR = register(ColorModifier.of("ambient_light_color", EnvironmentAttributes.AMBIENT_LIGHT_COLOR));
    public static final FloatModifier FOG_START_DISTANCE = register(FloatModifier.of("fog_start_distance", EnvironmentAttributes.FOG_START_DISTANCE));
    public static final FloatModifier FOG_END_DISTANCE = register(FloatModifier.nonNegative("fog_end_distance", EnvironmentAttributes.FOG_END_DISTANCE));
    public static final FloatModifier SKY_FOG_END_DISTANCE = register(FloatModifier.nonNegative("sky_fog_end_distance", EnvironmentAttributes.SKY_FOG_END_DISTANCE));
    public static final FloatModifier CLOUD_FOG_END_DISTANCE = register(FloatModifier.nonNegative("cloud_fog_end_distance", EnvironmentAttributes.CLOUD_FOG_END_DISTANCE));
    public static final FloatModifier WATER_FOG_START_DISTANCE = register(FloatModifier.of("water_fog_start_distance", EnvironmentAttributes.WATER_FOG_START_DISTANCE));
    public static final FloatModifier WATER_FOG_END_DISTANCE = register(FloatModifier.nonNegative("water_fog_end_distance", EnvironmentAttributes.WATER_FOG_END_DISTANCE));
    public static final FloatModifier CLOUD_HEIGHT =  register(FloatModifier.of("cloud_height", EnvironmentAttributes.CLOUD_HEIGHT));
    public static final FloatModifier STAR_BRIGHTNESS = register(FloatModifier.of("star_brightness", EnvironmentAttributes.STAR_BRIGHTNESS, 0, 1));
    public static final FloatModifier SKY_LIGHT_FACTOR = register(FloatModifier.of("sky_light_factor", EnvironmentAttributes.SKY_LIGHT_FACTOR, 0, 1));

    public static final FloatModifier SUN_ANGLE = register(FloatModifier.of("sun_angle", EnvironmentAttributes.SUN_ANGLE, 0, 360));
    public static final FloatModifier MOON_ANGLE = register(FloatModifier.of("moon_angle", EnvironmentAttributes.MOON_ANGLE, 0, 360));
    public static final FloatModifier STAR_ANGLE = register(FloatModifier.of("star_angle", EnvironmentAttributes.STAR_ANGLE, 0, 360));

    private static <T, B extends TerrainModifier<T>> B register(B modifier) {
        MODIFIERS_REGISTRY.register(modifier.id(), modifier);
        return modifier;
    }

}
