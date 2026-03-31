package com.unrealdinnerbone.weathergate;

import com.google.common.base.Suppliers;
import com.unrealdinnerbone.weathergate.modifers.base.TerrainModifier;
import com.unrealdinnerbone.weathergate.modifers.types.TerrainModifierType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.function.Supplier;

public class WeatherGateRegistries {

//    public static final ResourceKey<Registry<TerrainModifier<?>>> TERRAIN_MODIFIER_KEY = ResourceKey.createRegistryKey(WeatherGate.id("terrain_modifier"));
    public static final ResourceKey<Registry<TerrainModifierType<?, ?>>> KEY = ResourceKey.createRegistryKey(WeatherGate.id("modifiers"));
//    public static final DeferredRegister<TerrainModifier<?>> TERRAIN_MODIFIER_REGISTER = DeferredRegister.create(TERRAIN_MODIFIER_KEY, WeatherGate.MOD_ID);

    // Accessible after registries are frozen (i.e. at runtime, not class load)
//    public static final Supplier<Registry<TerrainModifier<?>>> TERRAIN_MODIFIER_REGISTRY = Suppliers.memoize(() -> {
//        return new RegistryBuilder<>(TERRAIN_MODIFIER_KEY)
//                .sync(true)
//                .create();
//    });

    public static final Registry<TerrainModifierType<?, ?>> REGISTRY = new RegistryBuilder<>(KEY)
            .sync(true)
            .create();
}