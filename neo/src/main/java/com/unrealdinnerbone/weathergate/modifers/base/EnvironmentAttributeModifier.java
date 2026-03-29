package com.unrealdinnerbone.weathergate.modifers.base;

import net.minecraft.world.attribute.EnvironmentAttribute;

public interface EnvironmentAttributeModifier<T, E> {

    E getAttributeValue(T value);

    EnvironmentAttribute<E> attribute();
}
