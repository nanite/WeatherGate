package com.unrealdinnerbone.weathergate.client.screen;

import com.unrealdinnerbone.weathergate.WeatherGateRegistries;
import com.unrealdinnerbone.weathergate.level.attachments.TerrainControllerAttachment;
import com.unrealdinnerbone.weathergate.level.attachments.terrain.ControllerData;
import com.unrealdinnerbone.weathergate.modifers.base.TerrainModifier;
import com.unrealdinnerbone.weathergate.modifers.types.TerrainModifierType;
import com.unrealdinnerbone.weathergate.network.packets.c2s.UpdateControllerPacket;
import com.unrealdinnerbone.weathergate.registry.ModifierTypes;
import dev.ftb.mods.ftblibrary.client.gui.screens.AbstractButtonListScreen;
import dev.ftb.mods.ftblibrary.client.gui.theme.NordColors;
import dev.ftb.mods.ftblibrary.client.gui.widget.BaseScreen;
import dev.ftb.mods.ftblibrary.client.gui.widget.ContextMenuItem;
import dev.ftb.mods.ftblibrary.client.gui.widget.Panel;
import dev.ftb.mods.ftblibrary.client.gui.widget.SimpleButton;
import dev.ftb.mods.ftblibrary.client.gui.widget.TextBox;
import dev.ftb.mods.ftblibrary.client.gui.widget.TextField;
import dev.ftb.mods.ftblibrary.client.gui.widget.Widget;
import dev.ftb.mods.ftblibrary.icon.Icons;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class TerrainControllerScreen extends AbstractButtonListScreen implements NordColors {

    private final BlockPos blockPos;
    public final ControllerData data;

    public TerrainControllerScreen(BlockPos blockPos) {
        super();
        this.blockPos = blockPos;
        ClientLevel level = Minecraft.getInstance().level;
        Biome biome = level.getBiome(blockPos).value();
        this.data = TerrainControllerAttachment.getAttachment(level).getData(blockPos);

        for (TerrainModifierType<?, ?> terrainModifier : WeatherGateRegistries.REGISTRY) {
            if (data.getModifiers().containsKey(terrainModifier)) {
                continue;
            }
            TerrainModifier<?> modifier = terrainModifier.create(level, biome, blockPos);
            data.getModifiers().put(terrainModifier, modifier);
        }

        setWidth(210);
        setHeight(210);
        showBottomPanel(false);
        setHasSearchBox(true);
    }

    @NotNull
    private <T> SimpleButton createSimpleButton(Panel panel, TerrainModifier<T> modifier, TerrainModifierType<T, ?> state) {
        return new EditButton<>(this, panel, state, modifier, modifier.getValue(), value -> sendUpdate(state, modifier.withValue(value)));
    }

    @Override
    protected void doCancel() {
    }

    @Override
    protected void doAccept() {
    }

    @Override
    public void addButtons(Panel panel) {
        data.getModifiers().entrySet().stream()
                .sorted(Map.Entry.comparingByKey(Comparator.comparing(ModifierTypes::getId)))
                .forEachOrdered(entry -> {
                    TerrainModifierType<?, ?> modifier = entry.getKey();
                    TerrainModifier<?> state = entry.getValue();
                    panel.add(new ModifierRow(panel, modifier, state));
                });
    }

    @Override
    protected Panel createTopPanel() {
        return new TerrainButtonListTopPanel();
    }

    private class ModifierRow<T> extends Panel {
        private final SimpleButton toggleButton;
        private final SimpleButton modifierButton;
        private final SimpleButton resetButton;
        private final Component searchText;
        private TerrainModifier<T> state;

        ModifierRow(Panel parent, TerrainModifierType<T, ?> modifier, TerrainModifier<T> state) {
            super(parent);
            this.searchText = Component.translatable(ModifierTypes.getId(modifier).toLanguageKey());
            this.state = state;
            toggleButton = new ImprovedToggleableButton(this, state.isEnabled(), Icons.ACCEPT, Icons.CANCEL, (widget, newState) -> {
                this.state = state.withEnabled(newState);
                sendUpdate(modifier, this.state);
            });
            this.resetButton = BetterButton.simple(this, Component.literal("Settings"), Icons.SETTINGS, () -> openContextMenu(List.of(
                    new ContextMenuItem(Component.literal("Reset to Default"), Icons.REFRESH, (btn) -> {
                        this.state = modifier.create(Minecraft.getInstance().level, Minecraft.getInstance().level.getBiome(blockPos).value(), blockPos);
                        sendUpdate(modifier, this.state);
                    })
            )));
            modifierButton = createSimpleButton( this, state, modifier);
            setHeight(16);
            setWidth(TerrainControllerScreen.this.width - 16 - 20);
        }

        @Override
        public void addWidgets() {
            add(toggleButton);
            add(modifierButton);
            add(resetButton);
        }

        @Override
        public void alignWidgets() {
            toggleButton.setPos(0, 0);
            toggleButton.setSize(16, 16);
            resetButton.setPos(this.width, 0);
            resetButton.setSize(16, 16);
            modifierButton.setPos(20, 0);
            modifierButton.setWidth(this.width - 16 - 6);
            modifierButton.setHeight(16);
        }

        @Override
        public Component getTitle() {
            return this.searchText;
        }
    }

    private class TerrainButtonListTopPanel extends ButtonListTopPanel {

        private final SimpleButton settingsButton;

        public TerrainButtonListTopPanel() {
            super();
            this.settingsButton = BetterButton.simple(this, Component.literal("Settings"), Icons.SETTINGS, () -> openContextMenu(List.of(
                    new ContextMenuItem(Component.literal("Reset All to Default"), Icons.REFRESH, (btn) -> {
                        data.getModifiers().clear();
                        GlobalPos controllerPos = GlobalPos.of(Minecraft.getInstance().player.level().dimension(), blockPos);
                        ClientPacketDistributor.sendToServer(new UpdateControllerPacket(controllerPos, data));
                        Minecraft.getInstance().levelRenderer.allChanged();
                    }),
                    new ContextMenuItem(Component.literal("Priority"), Icons.UP, (btn) -> {
                        IntEditPanel floatEditPanel = new IntEditPanel(this, data.getPriory(), newValue -> {
                            data.setPriory(newValue);
                            GlobalPos controllerPos = GlobalPos.of(Minecraft.getInstance().player.level().dimension(), blockPos);
                            ClientPacketDistributor.sendToServer(new UpdateControllerPacket(controllerPos, data));
                            Minecraft.getInstance().levelRenderer.allChanged();
                        });
                        floatEditPanel.setLimits(0, 1000);
                        BaseScreen gui = getGui();
                        int absX = Math.min(gui.getMouseX(), gui.getWindow().getGuiScaledWidth() - floatEditPanel.width - 10);
                        int absY = Math.min(gui.getMouseY(), gui.getWindow().getGuiScaledHeight() - floatEditPanel.height - 10);
                        floatEditPanel.setPos(absX - floatEditPanel.getParent().getX(), absY - floatEditPanel.getParent().getY());
                        gui.pushModalPanel(floatEditPanel);
                        floatEditPanel.setExtraZlevel(100);
                    }),
                    new ContextMenuItem(Component.literal("Range"), Icons.REFRESH, (btn) -> {
                        IntEditPanel floatEditPanel = new IntEditPanel(this, data.getRange(), newValue -> {
                            data.setRange(newValue);
                            GlobalPos controllerPos = GlobalPos.of(Minecraft.getInstance().player.level().dimension(), blockPos);
                            ClientPacketDistributor.sendToServer(new UpdateControllerPacket(controllerPos, data));
                            Minecraft.getInstance().levelRenderer.allChanged();
                        });
                        floatEditPanel.setLimits(1, 256);
                        BaseScreen gui = getGui();
                        int absX = Math.min(gui.getMouseX(), gui.getWindow().getGuiScaledWidth() - floatEditPanel.width - 10);
                        int absY = Math.min(gui.getMouseY(), gui.getWindow().getGuiScaledHeight() - floatEditPanel.height - 10);
                        floatEditPanel.setPos(absX - floatEditPanel.getParent().getX(), absY - floatEditPanel.getParent().getY());
                        gui.pushModalPanel(floatEditPanel);
                        floatEditPanel.setExtraZlevel(100);
                    })
            )));
        }

        @Override
        public void addWidgets() {
            super.addWidgets();
            add(settingsButton);
        }

        @Override
        public void alignWidgets() {
            super.alignWidgets();
            for (Widget widget : widgets) {
                if (widget instanceof TextBox || widget instanceof TextField) {
                    widget.setWidth(widget.width - 16 - 4);
                }
            }
            settingsButton.setPos(width - 16 - 4, 7);
            settingsButton.setSize(16, 16);
        }
    }

    private <T> void sendUpdate(TerrainModifierType<T, ?> type, TerrainModifier<T> modifier) {
        data.getModifiers().put(type, modifier);
        GlobalPos controllerPos = GlobalPos.of(Minecraft.getInstance().player.level().dimension(), blockPos);
        ClientPacketDistributor.sendToServer(new UpdateControllerPacket(controllerPos, data));
        if (type.requireReRender()) {
            Minecraft.getInstance().levelRenderer.allChanged();
        }
        this.refreshWidgets();
    }
}
