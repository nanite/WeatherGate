package com.unrealdinnerbone.weathergate.level.attachments;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.unrealdinnerbone.weathergate.WeatherGateCodecs;
import com.unrealdinnerbone.weathergate.WeatherGateRegistry;
import com.unrealdinnerbone.weathergate.level.attachments.terrain.ControllerData;
import com.unrealdinnerbone.weathergate.level.attachments.terrain.ModifierState;
import com.unrealdinnerbone.weathergate.level.attachments.terrain.StoredData;
import com.unrealdinnerbone.weathergate.modifers.base.TerrainModifier;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TerrainControllerAttachment {

    public static final TerrainControllerAttachment EMPTY = new TerrainControllerAttachment(new HashMap<>());
    private final Map<BlockPos, ControllerData> data;

    private TerrainControllerAttachment(Map<BlockPos, ControllerData> data) {
        this.data = data;
    }

    public static TerrainControllerAttachment of(Map<BlockPos, ControllerData> data) {
        return new TerrainControllerAttachment(new HashMap<>(data));
    }

    public static final MapCodec<TerrainControllerAttachment> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            Codec.unboundedMap(WeatherGateCodecs.BLOCK_POS_CODEC, ControllerData.CODEC)
                    .fieldOf("data")
                    .forGetter(terrainControllerAttachment -> terrainControllerAttachment.data)
    ).apply(i, TerrainControllerAttachment::of));

    public static final StreamCodec<RegistryFriendlyByteBuf, TerrainControllerAttachment> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.map(HashMap::new, BlockPos.STREAM_CODEC, ControllerData.STREAM_CODEC),
            terrainControllerAttachment -> terrainControllerAttachment.data,
            TerrainControllerAttachment::of
    );


    public void save(Level level) {
        level.setData(WeatherGateRegistry.TERIANN_CONTROLLER_ATTACHMENT.entryValue(), this);
    }

    public static TerrainControllerAttachment getAttachment(Level level) {
        return level.getData(WeatherGateRegistry.TERIANN_CONTROLLER_ATTACHMENT.entryValue());
    }

    public void setData(BlockPos pos, ControllerData controllerData) {
        data.put(pos, controllerData);
    }

    public ControllerData getData(BlockPos pos) {
        return data.computeIfAbsent(pos, _ -> ControllerData.defaultData());
    }

    public void removeData(BlockPos pos) {
        data.remove(pos);
    }

    public Set<Map.Entry<BlockPos, ControllerData>> entrySet() {
        return data.entrySet();
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public <T> T getModifierValue(BlockPos pos, TerrainModifier<T> modifier) {
        ControllerData controllerData = data.get(pos);
        if (controllerData == null) {
            return null;
        }

        StoredData storedData = controllerData.getStoredData();
        ModifierState<?> state = storedData.modifiers().get(modifier);
        if (state == null || !state.enabled()) {
            return null;
        }

        return (T) state.value();
    }


}
