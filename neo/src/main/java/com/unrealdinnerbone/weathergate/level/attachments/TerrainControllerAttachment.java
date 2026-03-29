package com.unrealdinnerbone.weathergate.level.attachments;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.unrealdinnerbone.weathergate.WeatherGateRegistry;
import com.unrealdinnerbone.weathergate.registry.TerrainModifiers;
import com.unrealdinnerbone.weathergate.modifers.base.TerrainModifier;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;

public record TerrainControllerAttachment(Map<BlockPos, StoredData> data) {

    public static TerrainControllerAttachment of(Map<BlockPos, StoredData> data) {
        return new TerrainControllerAttachment(new HashMap<>(data));
    }

    public record ModifierState<T>(boolean enabled, T value) {
        public ModifierState<T> withEnabled(boolean enabled) {
            return new ModifierState<>(enabled, value);
        }

        public ModifierState<T> withValue(T value) {
            return new ModifierState<>(enabled, value);
        }
    }

    public record StoredData(Map<TerrainModifier<?>, ModifierState<?>> modifiers) {

        public static StoredData of(Map<TerrainModifier<?>, ModifierState<?>> modifiers) {
            return new StoredData(new HashMap<>(modifiers));
        }

        private record ModifierEntry(TerrainModifier<?> modifier, ModifierState<?> state) {}

        @SuppressWarnings("unchecked")
        private static MapCodec<ModifierEntry> entryCodecFor(TerrainModifier<?> modifier) {
            Codec<Object> valueCodec = (Codec<Object>) modifier.getCodec();

            return RecordCodecBuilder.mapCodec(instance -> instance.group(
                    Codec.BOOL.optionalFieldOf("enabled", true).forGetter(entry -> entry.state().enabled()),
                    valueCodec.fieldOf("value").forGetter(entry -> (Object) entry.state().value())
            ).apply(instance, (enabled, value) ->
                    new ModifierEntry(modifier, new ModifierState<>(enabled, value))
            ));
        }

        private static final Codec<ModifierEntry> ENTRY_CODEC = TerrainModifiers.MODIFIERS_REGISTRY.dispatch(
                "modifier",
                ModifierEntry::modifier,
                StoredData::entryCodecFor
        );

        public static final Codec<StoredData> CODEC = Codec.list(ENTRY_CODEC).xmap(
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
    }

    public static final Codec<BlockPos> BLOCK_POS_CODEC = Codec.STRING.xmap(
            s -> BlockPos.of(Long.parseLong(s)),
            pos -> Long.toString(pos.asLong())
    );

    public static final MapCodec<TerrainControllerAttachment> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            Codec.unboundedMap(BLOCK_POS_CODEC, StoredData.CODEC)
                    .fieldOf("data")
                    .forGetter(TerrainControllerAttachment::data)
    ).apply(i, TerrainControllerAttachment::of));

    public static final StreamCodec<RegistryFriendlyByteBuf, TerrainControllerAttachment> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.map(HashMap::new, BlockPos.STREAM_CODEC, StoredData.STREAM_CODEC),
            TerrainControllerAttachment::data,
            TerrainControllerAttachment::of
    );

    public static final TerrainControllerAttachment EMPTY = new TerrainControllerAttachment(new HashMap<>());

    public void save(Level level) {
        level.setData(WeatherGateRegistry.TERIANN_CONTROLLER_ATTACHMENT.entryValue(), this);
    }

    public static TerrainControllerAttachment getAttachment(Level level) {
        return level.getData(WeatherGateRegistry.TERIANN_CONTROLLER_ATTACHMENT.entryValue());
    }

    public <T> void setData(BlockPos pos, TerrainModifier<T> modifier, T value) {
        StoredData storedData = data.computeIfAbsent(pos, ignored -> new StoredData(new HashMap<>()));
        ModifierState<?> existingState = storedData.modifiers().get(modifier);
        boolean enabled = existingState == null || existingState.enabled();
        storedData.modifiers().put(modifier, new ModifierState<>(enabled, value));
    }

    public void setEnabled(BlockPos pos, TerrainModifier<?> modifier, boolean enabled) {
        StoredData storedData = data.get(pos);
        if (storedData == null) return;

        ModifierState<?> existingState = storedData.modifiers().get(modifier);
        if (existingState == null) return;

        storedData.modifiers().put(modifier, new ModifierState<>(enabled, existingState.value()));
    }

    public boolean isEnabled(BlockPos pos, TerrainModifier<?> modifier) {
        StoredData storedData = data.get(pos);
        if (storedData == null) return false;

        ModifierState<?> state = storedData.modifiers().get(modifier);
        return state != null && state.enabled();
    }

    @SuppressWarnings("unchecked")
    public <T> ModifierState<T> getState(BlockPos pos, TerrainModifier<T> modifier) {
        StoredData storedData = data.get(pos);
        if (storedData == null) return null;

        return (ModifierState<T>) storedData.modifiers().get(modifier);
    }

    @SuppressWarnings("unchecked")
    public <T> T getData(BlockPos pos, TerrainModifier<T> modifier) {
        StoredData storedData = data.get(pos);
        if (storedData == null) return null;

        ModifierState<?> state = storedData.modifiers().get(modifier);
        if (state == null || !state.enabled()) return null;

        return (T) state.value();
    }
}
