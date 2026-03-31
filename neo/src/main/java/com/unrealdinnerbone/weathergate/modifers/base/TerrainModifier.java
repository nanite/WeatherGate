package com.unrealdinnerbone.weathergate.modifers.base;

import com.unrealdinnerbone.weathergate.modifers.types.TerrainModifierType;
import dev.ftb.mods.ftblibrary.client.gui.widget.ModalPanel;
import dev.ftb.mods.ftblibrary.client.gui.widget.Panel;
import dev.ftb.mods.ftblibrary.icon.Icon;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

import java.util.function.Consumer;

public interface TerrainModifier<T> {

    TerrainModifierType<T, ?> type();

    boolean isEnabled();

    Icon<?> getIcon(T value);

    ModalPanel createEditPanel(Panel basePanel, T activeValue, Consumer<T> newValueApplier);

    T getValue();

    TerrainModifier<T> withValue(T value);

    TerrainModifier<T> withEnabled(boolean enabled);
}
