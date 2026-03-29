package com.unrealdinnerbone.weathergate.level.attachments.terrain;

import com.mojang.serialization.Codec;
import com.unrealdinnerbone.weathergate.modifers.base.TerrainModifier;
import com.unrealdinnerbone.weathergate.registry.TerrainModifiers;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.HashMap;
import java.util.Map;

public class StoredData {

    public static StoredData of(Map<TerrainModifier<?>, ModifierState<?>> modifiers) {
        return new StoredData(new HashMap<>(modifiers));
    }

    public static final StoredData empty() {
        return new StoredData(new HashMap<>());
    }

    public static final Codec<StoredData> CODEC = Codec.list(ModifierEntry.CODEC).xmap(
            entries -> {
                Map<TerrainModifier<?>, ModifierState<?>> map = new HashMap<>();
                for (ModifierEntry entry : entries) {
                    map.put(entry.modifier(), entry.state());
                }
                return StoredData.of(map);
            },
            storedData -> storedData.modifiers().entrySet().stream()
                    .map(entry -> new ModifierEntry(entry.getKey(), entry.getValue()))
                    .toList()
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, StoredData> STREAM_CODEC = StreamCodec.of(
            (buf, storedData) -> {
                buf.writeVarInt(storedData.modifiers().size());
                for (Map.Entry<TerrainModifier<?>, ModifierState<?>> entry : storedData.modifiers().entrySet()) {
                    TerrainModifier<?> modifier = entry.getKey();
                    ModifierState<?> state = entry.getValue();

                    TerrainModifiers.STREAM_CODEC.encode(buf, modifier);
                    buf.writeBoolean(state.enabled());
                    encodeValue(buf, modifier, state.value());
                }
            },
            buf -> {
                int size = buf.readVarInt();
                Map<TerrainModifier<?>, ModifierState<?>> map = new HashMap<>(size);

                for (int i = 0; i < size; i++) {
                    TerrainModifier<?> modifier = TerrainModifiers.STREAM_CODEC.decode(buf);
                    boolean enabled = buf.readBoolean();
                    Object value = decodeValue(buf, modifier);

                    map.put(modifier, new ModifierState<>(enabled, value));
                }

                return StoredData.of(map);
            }
    );

    private final Map<TerrainModifier<?>, ModifierState<?>> modifiers;

    public StoredData(Map<TerrainModifier<?>, ModifierState<?>> modifiers) {
        this.modifiers = modifiers;
    }

    @SuppressWarnings("unchecked")
    private static void encodeValue(RegistryFriendlyByteBuf buf, TerrainModifier<?> rawModifier, Object value) {
        TerrainModifier<Object> modifier = (TerrainModifier<Object>) rawModifier;
        modifier.getStreamCodec().encode(buf, value);
    }

    @SuppressWarnings("unchecked")
    private static Object decodeValue(RegistryFriendlyByteBuf buf, TerrainModifier<?> rawModifier) {
        TerrainModifier<Object> modifier = (TerrainModifier<Object>) rawModifier;
        return modifier.getStreamCodec().decode(buf);
    }

    public Map<TerrainModifier<?>, ModifierState<?>> modifiers() {
        return modifiers;
    }

}