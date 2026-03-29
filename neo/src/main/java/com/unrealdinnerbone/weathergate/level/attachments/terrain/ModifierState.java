package com.unrealdinnerbone.weathergate.level.attachments.terrain;


public record ModifierState<T>(boolean enabled, T value) {

    public ModifierState<T> withEnabled(boolean enabled) {
        return new ModifierState<>(enabled, value);
    }

    public ModifierState<T> withValue(T value) {
        return new ModifierState<>(enabled, value);
    }
}