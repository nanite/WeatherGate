package com.unrealdinnerbone.weathergate.client.screen;

import com.unrealdinnerbone.weathergate.level.attachments.TerrainControllerAttachment;
import com.unrealdinnerbone.weathergate.modifers.base.TerrainModifier;
import com.unrealdinnerbone.weathergate.network.packets.c2s.UpdateControllerPacket;
import com.unrealdinnerbone.weathergate.registry.TerrainModifiers;
import dev.ftb.mods.ftblibrary.client.gui.screens.AbstractButtonListScreen;
import dev.ftb.mods.ftblibrary.client.gui.theme.NordColors;
import dev.ftb.mods.ftblibrary.client.gui.widget.Panel;
import dev.ftb.mods.ftblibrary.client.gui.widget.SimpleButton;
import dev.ftb.mods.ftblibrary.icon.Icons;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class TerrainControllerScreen2 extends AbstractButtonListScreen implements NordColors {

    private final BlockPos blockPos;
    public final TerrainControllerAttachment.StoredData data;

    public TerrainControllerScreen2(BlockPos blockPos) {
        super();
        this.blockPos = blockPos;
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            data = new TerrainControllerAttachment.StoredData(new HashMap<>());
            return;
        };
        Biome biome = level.getBiome(blockPos).value();
        this.data = TerrainControllerAttachment.getAttachment(level)
                .data()
                .computeIfAbsent(blockPos, ignored -> new TerrainControllerAttachment.StoredData(new HashMap<>()));

        for (TerrainModifier<?> terrainModifier : TerrainModifiers.MODIFIERS_REGISTRY) {
            if (data.modifiers().containsKey(terrainModifier)) continue;
            data.modifiers().put(terrainModifier, new TerrainControllerAttachment.ModifierState<>(terrainModifier.isEnabledByDefault(), terrainModifier.getDefaultValue(level, biome, blockPos)));
        }

        setWidth(210);
        setHeight(210);
        showBottomPanel(false);
    }

    @NotNull
    private <T> SimpleButton createSimpleButton(Panel panel, TerrainModifier<T> modifier, TerrainControllerAttachment.ModifierState<T> state) {
        return new EditButton<>(this, panel, modifier, state.value(), value -> sendUpdate(modifier, state.withValue(value)));
    }

    @Override
    protected void doCancel() {
    }

    @Override
    protected void doAccept() {
    }

    @Override
    public void addButtons(Panel panel) {
        data.modifiers().entrySet().stream()
                .sorted(Map.Entry.comparingByKey(Comparator.comparing(TerrainModifier::id)))
                .forEachOrdered(entry -> {
                    TerrainModifier<?> modifier = entry.getKey();
                    TerrainControllerAttachment.ModifierState<?> state = entry.getValue();
                    panel.add(new ModifierRow(panel, modifier, state));
                });
    }

    private class ModifierRow<T> extends Panel {
        private final SimpleButton toggleButton;
        private final SimpleButton modifierButton;
        private TerrainControllerAttachment.ModifierState<T> state;

        ModifierRow(Panel parent, TerrainModifier<T> modifier, TerrainControllerAttachment.ModifierState<T> state) {
            super(parent);
            this.state = state;
            toggleButton = new ImprovedToggleableButton(this, state.enabled(), Icons.ACCEPT, Icons.CANCEL, (widget, newState) -> {
                this.state = state.withEnabled(newState);
                sendUpdate(modifier, this.state);
            });
            modifierButton = createSimpleButton( this, modifier, state);
            setHeight(16);
            setWidth(TerrainControllerScreen2.this.width - 16 - 20);
        }

        @Override
        public void addWidgets() {
            add(toggleButton);
            add(modifierButton);
        }

        @Override
        public void alignWidgets() {
            toggleButton.setPos(0, 0);
            toggleButton.setSize(16, 16);
            modifierButton.setPos(20, 0);
            modifierButton.setWidth(TerrainControllerScreen2.this.width - 20);
            modifierButton.setHeight(16);
        }
    }

    private void sendUpdate(TerrainModifier<?> modifier, TerrainControllerAttachment.ModifierState<?> newState) {
        data.modifiers().put(modifier, newState);
        GlobalPos controllerPos = GlobalPos.of(Minecraft.getInstance().player.level().dimension(), blockPos);
        TerrainControllerAttachment.StoredData payload = TerrainControllerAttachment.StoredData.of(Map.of(modifier, newState));
        ClientPacketDistributor.sendToServer(new UpdateControllerPacket(controllerPos, payload));
        if (modifier.requireReRender()) {
            Minecraft.getInstance().levelRenderer.allChanged();
        }
        this.refreshWidgets();
    }
}
