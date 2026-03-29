package com.unrealdinnerbone.weathergate.modifers.base;

public interface EnvironmentAttributeModifier<T, E> {

    E getAttributeValue(T value);
}
