package com.unrealdinnerbone.weathergate.level.attachments.terrain;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.unrealdinnerbone.weathergate.WeatherGateRegistries;
import com.unrealdinnerbone.weathergate.modifers.base.TerrainModifier;
import com.unrealdinnerbone.weathergate.modifers.types.TerrainModifierType;
import com.unrealdinnerbone.weathergate.registry.ModifierTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import org.jspecify.annotations.NullMarked;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@NullMarked
public class ControllerData {

    public static final int DEFAULT_RANGE = 64;

    public static ControllerData defaultData() {
        return new ControllerData(DEFAULT_RANGE, new HashMap<>());
    }

    public static final Codec<ControllerData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("range").forGetter(ControllerData::getRange),
            Codec.unboundedMap(WeatherGateRegistries.REGISTRY.byNameCodec(), ModifierTypes.CODEC).fieldOf("data").forGetter(ControllerData::getModifiers)

    ).apply(instance, ControllerData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ControllerData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            ControllerData::getRange,
            ByteBufCodecs.map(HashMap::new,
                    ByteBufCodecs.registry(WeatherGateRegistries.KEY),
                    ModifierTypes.STREAM_CODEC),
            ControllerData::getModifiers,
            ControllerData::new
    );

    private int range;
    private final Map<TerrainModifierType<?, ?>, TerrainModifier<?>> modifiers;

    public ControllerData(int range, Map<TerrainModifierType<?, ?>, TerrainModifier<?>> modifiers) {
        this.range = range;
        this.modifiers = modifiers;
    }

    public Map<TerrainModifierType<?, ?>, TerrainModifier<?>> getModifiers() {
        return modifiers;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ControllerData that = (ControllerData) o;
        return range == that.range && Objects.equals(modifiers, that.modifiers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(range, modifiers);
    }
}
