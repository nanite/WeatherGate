package com.unrealdinnerbone.weathergate.registry;

import com.mojang.serialization.Codec;
import com.unrealdinnerbone.weathergate.WeatherGate;
import com.unrealdinnerbone.weathergate.WeatherGateRegistries;
import com.unrealdinnerbone.weathergate.modifers.AlphaColorModifier;
import com.unrealdinnerbone.weathergate.modifers.ColorModifier;
import com.unrealdinnerbone.weathergate.modifers.FloatModifier;
import com.unrealdinnerbone.weathergate.modifers.base.TerrainModifier;
import com.unrealdinnerbone.weathergate.modifers.types.AlphaColorEnvironmentType;
import com.unrealdinnerbone.weathergate.modifers.types.ColorEnvironmentType;
import com.unrealdinnerbone.weathergate.modifers.types.DryFoliageColorTerrainModifier;
import com.unrealdinnerbone.weathergate.modifers.types.FloatEnvironmentType;
import com.unrealdinnerbone.weathergate.modifers.types.FoliageColorTerrainModifier;
import com.unrealdinnerbone.weathergate.modifers.types.GrassColorTerrainModifier;
import com.unrealdinnerbone.weathergate.modifers.types.TerrainModifierType;
import com.unrealdinnerbone.weathergate.modifers.types.WaterColorTerrainModifier;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModifierTypes {

    public static final DeferredRegister<TerrainModifierType<?, ?>> REGISTER = DeferredRegister.create(WeatherGateRegistries.KEY, WeatherGate.MOD_ID);

    public static final Codec<TerrainModifier<?>> CODEC = WeatherGateRegistries.REGISTRY.byNameCodec().dispatch("type", TerrainModifier::type, TerrainModifierType::codec);
    public static final StreamCodec<RegistryFriendlyByteBuf, TerrainModifier<?>> STREAM_CODEC = ByteBufCodecs.registry(WeatherGateRegistries.KEY).dispatch(TerrainModifier::type, TerrainModifierType::streamCodec);

    public static final DeferredHolder<TerrainModifierType<?, ?>, TerrainModifierType<Color4I, ColorModifier>> FOG_COLOR = REGISTER.register("fog_color", () -> ColorEnvironmentType.of(EnvironmentAttributes.FOG_COLOR));
    public static final DeferredHolder<TerrainModifierType<?, ?>, TerrainModifierType<Color4I, ColorModifier>> WATER_FOG_COLOR = REGISTER.register("water_fog_color", () -> ColorEnvironmentType.of(EnvironmentAttributes.WATER_FOG_COLOR));
    public static final DeferredHolder<TerrainModifierType<?, ?>, TerrainModifierType<Color4I, ColorModifier>> SKY_COLOR = REGISTER.register("sky_color", () -> ColorEnvironmentType.of(EnvironmentAttributes.SKY_COLOR));
    public static final DeferredHolder<TerrainModifierType<?, ?>, TerrainModifierType<Color4I, ColorModifier>> BLOCK_LIGHT_TINT = REGISTER.register("block_light_tint", () -> ColorEnvironmentType.of(EnvironmentAttributes.BLOCK_LIGHT_TINT));
    public static final DeferredHolder<TerrainModifierType<?, ?>, TerrainModifierType<Color4I, ColorModifier>> SKY_LIGHT_COLOR = REGISTER.register("sky_light_color", () -> ColorEnvironmentType.of(EnvironmentAttributes.SKY_LIGHT_COLOR));
    public static final DeferredHolder<TerrainModifierType<?, ?>, TerrainModifierType<Color4I, ColorModifier>> NIGHT_VISION_COLOR = REGISTER.register("night_vision_color", () -> ColorEnvironmentType.of(EnvironmentAttributes.NIGHT_VISION_COLOR));
    public static final DeferredHolder<TerrainModifierType<?, ?>, TerrainModifierType<Color4I, ColorModifier>> AMBIENT_LIGHT_COLOR = REGISTER.register("ambient_light_color", () -> ColorEnvironmentType.of(EnvironmentAttributes.AMBIENT_LIGHT_COLOR));

    public static final DeferredHolder<TerrainModifierType<?, ?>, TerrainModifierType<Color4I, AlphaColorModifier>> SUNRISE_SUNSET_COLOR = REGISTER.register("sunrise_sunset_color", () -> AlphaColorEnvironmentType.of(EnvironmentAttributes.SUNRISE_SUNSET_COLOR));
    public static final DeferredHolder<TerrainModifierType<?, ?>, TerrainModifierType<Color4I, AlphaColorModifier>> CLOUD_COLOR = REGISTER.register("cloud_color", () -> AlphaColorEnvironmentType.of(EnvironmentAttributes.CLOUD_COLOR));

    public static final DeferredHolder<TerrainModifierType<?, ?>, TerrainModifierType<Float, FloatModifier>> FOG_START_DISTANCE = REGISTER.register("fog_start_distance", () -> FloatEnvironmentType.of(EnvironmentAttributes.FOG_START_DISTANCE));
    public static final DeferredHolder<TerrainModifierType<?, ?>, TerrainModifierType<Float, FloatModifier>> FOG_END_DISTANCE = REGISTER.register("fog_end_distance", () -> FloatEnvironmentType.nonNegative(EnvironmentAttributes.FOG_END_DISTANCE));
    public static final DeferredHolder<TerrainModifierType<?, ?>, TerrainModifierType<Float, FloatModifier>> SKY_FOG_END_DISTANCE = REGISTER.register("sky_fog_end_distance", () -> FloatEnvironmentType.nonNegative(EnvironmentAttributes.SKY_FOG_END_DISTANCE));
    public static final DeferredHolder<TerrainModifierType<?, ?>, TerrainModifierType<Float, FloatModifier>> CLOUD_FOG_END_DISTANCE = REGISTER.register("cloud_fog_end_distance", () -> FloatEnvironmentType.nonNegative(EnvironmentAttributes.CLOUD_FOG_END_DISTANCE));
    public static final DeferredHolder<TerrainModifierType<?, ?>, TerrainModifierType<Float, FloatModifier>> WATER_FOG_START_DISTANCE = REGISTER.register("water_fog_start_distance", () -> FloatEnvironmentType.of(EnvironmentAttributes.WATER_FOG_START_DISTANCE));
    public static final DeferredHolder<TerrainModifierType<?, ?>, TerrainModifierType<Float, FloatModifier>> WATER_FOG_END_DISTANCE = REGISTER.register("water_fog_end_distance", () -> FloatEnvironmentType.nonNegative(EnvironmentAttributes.WATER_FOG_END_DISTANCE));
    public static final DeferredHolder<TerrainModifierType<?, ?>, TerrainModifierType<Float, FloatModifier>> CLOUD_HEIGHT = REGISTER.register("cloud_height", () -> FloatEnvironmentType.of(EnvironmentAttributes.CLOUD_HEIGHT));
    public static final DeferredHolder<TerrainModifierType<?, ?>, TerrainModifierType<Float, FloatModifier>> STAR_BRIGHTNESS = REGISTER.register("star_brightness", () -> FloatEnvironmentType.of(EnvironmentAttributes.STAR_BRIGHTNESS, 0, 1));
    public static final DeferredHolder<TerrainModifierType<?, ?>, TerrainModifierType<Float, FloatModifier>> SKY_LIGHT_FACTOR = REGISTER.register("sky_light_factor", () -> FloatEnvironmentType.of(EnvironmentAttributes.SKY_LIGHT_FACTOR, 0, 1));
    public static final DeferredHolder<TerrainModifierType<?, ?>, TerrainModifierType<Float, FloatModifier>> SUN_ANGLE = REGISTER.register("sun_angle", () -> FloatEnvironmentType.of(EnvironmentAttributes.SUN_ANGLE, 0, 360));
    public static final DeferredHolder<TerrainModifierType<?, ?>, TerrainModifierType<Float, FloatModifier>> MOON_ANGLE = REGISTER.register("moon_angle", () -> FloatEnvironmentType.of(EnvironmentAttributes.MOON_ANGLE, 0, 360));
    public static final DeferredHolder<TerrainModifierType<?, ?>, TerrainModifierType<Float, FloatModifier>> STAR_ANGLE = REGISTER.register("star_angle", () -> FloatEnvironmentType.of(EnvironmentAttributes.STAR_ANGLE, 0, 360));

    public static final DeferredHolder<TerrainModifierType<?, ?>, DryFoliageColorTerrainModifier> DRY_FOLIAGE = REGISTER.register("dry_foliage", DryFoliageColorTerrainModifier::new);
    public static final DeferredHolder<TerrainModifierType<?, ?>, GrassColorTerrainModifier> GRASS = REGISTER.register("grass", GrassColorTerrainModifier::new);
    public static final DeferredHolder<TerrainModifierType<?, ?>, FoliageColorTerrainModifier> FOLIAGE = REGISTER.register("foliage", FoliageColorTerrainModifier::new);
    public static final DeferredHolder<TerrainModifierType<?, ?>, WaterColorTerrainModifier> WATER = REGISTER.register("water", WaterColorTerrainModifier::new);

    public static void init() {
    }

    public static Identifier getId(TerrainModifierType<?, ?> type) {
        return WeatherGateRegistries.REGISTRY.getResourceKey(type).map(ResourceKey::identifier).orElseThrow();
    }

}


