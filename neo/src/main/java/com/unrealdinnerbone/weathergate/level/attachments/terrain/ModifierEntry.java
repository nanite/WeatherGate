package com.unrealdinnerbone.weathergate.level.attachments.terrain;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.unrealdinnerbone.weathergate.modifers.base.TerrainModifier;
import com.unrealdinnerbone.weathergate.registry.TerrainModifiers;

public record ModifierEntry(TerrainModifier<?> modifier, ModifierState<?> state) {

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

    public static final Codec<ModifierEntry> CODEC = TerrainModifiers.MODIFIERS_REGISTRY.dispatch(
            "modifier",
            ModifierEntry::modifier,
            ModifierEntry::entryCodecFor
    );
}
