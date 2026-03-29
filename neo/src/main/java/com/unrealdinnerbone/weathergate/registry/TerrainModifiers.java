package com.unrealdinnerbone.weathergate.registry;

import com.unrealdinnerbone.weathergate.modifers.AlphaColorModifier;
import com.unrealdinnerbone.weathergate.modifers.ColorModifier;
import com.unrealdinnerbone.weathergate.modifers.FoliageColorTerrainModifier;
import com.unrealdinnerbone.weathergate.modifers.GrassColorTerrainModifier;
import com.unrealdinnerbone.weathergate.modifers.WaterColorTerrainModifier;
import com.unrealdinnerbone.weathergate.modifers.base.TerrainModifier;
import com.unrealdinnerbone.weathergate.util.CodecRegistry;
import dev.ftb.mods.ftblibrary.icon.Color4I;
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

    public static final TerrainModifier<Color4I> GRASS = register(new GrassColorTerrainModifier());
    public static final TerrainModifier<Color4I> FOLIAGE = register(new FoliageColorTerrainModifier());
    public static final TerrainModifier<Color4I> WATER = register(new WaterColorTerrainModifier());
    public static final TerrainModifier<Color4I> FOG_COLOR = register(ColorModifier.of("fog_color", EnvironmentAttributes.FOG_COLOR));
    public static final TerrainModifier<Color4I> WATER_FOG_COLOR = register(ColorModifier.of("water_fog_color", EnvironmentAttributes.WATER_FOG_COLOR));
    public static final TerrainModifier<Color4I> SKY_COLOR = register(ColorModifier.of("sky_color", EnvironmentAttributes.SKY_COLOR));
    public static final TerrainModifier<Color4I> SUNRISE_SUNSET_COLOR = register(AlphaColorModifier.of("sunrise_sunset_color", EnvironmentAttributes.SUNRISE_SUNSET_COLOR));
    public static final TerrainModifier<Color4I> CLOUD_COLOR = register(AlphaColorModifier.of("cloud_color", EnvironmentAttributes.FOG_COLOR));
    public static final TerrainModifier<Color4I> BLOCK_LIGHT_TINT = register(ColorModifier.of("block_light_tint", EnvironmentAttributes.BLOCK_LIGHT_TINT));
    public static final TerrainModifier<Color4I> SKY_LIGHT_COLOR = register(ColorModifier.of("sky_light_color", EnvironmentAttributes.SKY_LIGHT_COLOR));
    public static final TerrainModifier<Color4I> NIGHT_VISION_COLOR = register(ColorModifier.of("night_vision_color", EnvironmentAttributes.NIGHT_VISION_COLOR));
    public static final TerrainModifier<Color4I> AMBIENT_LIGHT_COLOR = register(ColorModifier.of("ambient_light_color", EnvironmentAttributes.AMBIENT_LIGHT_COLOR));


    private static TerrainModifier<Color4I> register(TerrainModifier<Color4I> modifier) {
        MODIFIERS_REGISTRY.register(modifier.id(), modifier);
        return modifier;
    }

}
