package com.unrealdinnerbone.weathergate.client.screen;

import com.unrealdinnerbone.weathergate.level.attachments.TerrainControllerAttachment;
import com.unrealdinnerbone.weathergate.modifers.base.TerrainModifier;
import com.unrealdinnerbone.weathergate.network.packets.c2s.UpdateControllerPacket;
import dev.ftb.mods.ftblibrary.client.gui.screens.AbstractButtonListScreen;
import dev.ftb.mods.ftblibrary.client.gui.theme.NordColors;
import dev.ftb.mods.ftblibrary.client.gui.theme.Theme;
import dev.ftb.mods.ftblibrary.client.gui.widget.Panel;
import dev.ftb.mods.ftblibrary.client.gui.widget.SimpleButton;
import dev.ftb.mods.ftblibrary.client.icon.IconHelper;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.icon.Icons;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class TerrainControllerScreen2 extends AbstractButtonListScreen implements NordColors {

    private final BlockPos blockPos;
    public final TerrainControllerAttachment.StoredData data;

    public TerrainControllerScreen2(BlockPos blockPos) {
        super();
        this.blockPos = blockPos;
        this.data = TerrainControllerAttachment.getAttachment(Minecraft.getInstance().level)
                .data()
                .computeIfAbsent(blockPos, ignored -> new TerrainControllerAttachment.StoredData(new HashMap<>()));

        setWidth(210);
        setHeight(210);
        showBottomPanel(false);
    }

    @NotNull
    @SuppressWarnings("unchecked")
    private <T> SimpleButton createSimpleButton(
            Panel panelA,
            Panel panel,
            TerrainControllerScreen2 terrainControllerScreen2,
            TerrainModifier<T> modifier,
            TerrainControllerAttachment.ModifierState<?> state
    ) {
        return modifier.createButton(
                panelA,
                terrainControllerScreen2,
                panel,
                blockPos,
                (TerrainControllerAttachment.ModifierState<T>) state
        );
    }

    @Override
    protected void doCancel() {
    }

    @Override
    protected void doAccept() {
    }

    @Override
    public void addButtons(Panel panel) {
        for (Map.Entry<TerrainModifier<?>, TerrainControllerAttachment.ModifierState<?>> entry : data.modifiers().entrySet()) {
            panel.add(new ModifierRow(panel, TerrainControllerScreen2.this, entry.getKey(), entry.getValue()));
        }
    }

    private class ModifierRow extends Panel {
        private final TerrainModifier<?> modifier;
        private final SimpleButton toggleButton;
        private final SimpleButton modifierButton;
        private TerrainControllerAttachment.ModifierState<?> state;

        ModifierRow(Panel parent, TerrainControllerScreen2 screen2, TerrainModifier<?> modifier, TerrainControllerAttachment.ModifierState<?> state) {
            super(parent);
            this.modifier = modifier;
            this.state = state;
            toggleButton = new ToggleButton(this);
            modifierButton = createSimpleButton(parent, this, screen2, modifier, state);
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

        private class ToggleButton extends SimpleButton {
            ToggleButton(Panel parent) {
                super(parent, Component.empty(), Icon.empty(), (btn, mouseButton) -> {});
                setConsumer((btn, mouse) -> toggle());
            }

            private void toggle() {
                boolean newValue = !state.enabled();
                state = state.withEnabled(newValue);
                data.modifiers().put(modifier, state);
                TerrainControllerScreen2.this.refreshWidgets();
                sendUpdate(state);
            }

            @Override
            public void draw(GuiGraphicsExtractor gfx, Theme theme, int x, int y, int w, int h) {
                super.draw(gfx, theme, x, y, w, h);
                theme.drawWidget(gfx, x, y, w, h, getWidgetType());
                Icon<?> checkIcon = state.enabled() ? Icons.ACCEPT : Icons.CANCEL;
                IconHelper.renderIcon(checkIcon, gfx, x + 3, y + 3, w - 6, h - 6);
            }
        }

        private void sendUpdate(TerrainControllerAttachment.ModifierState<?> newState) {
            GlobalPos controllerPos = GlobalPos.of(
                    Minecraft.getInstance().player.level().dimension(),
                    blockPos
            );
            TerrainControllerAttachment.StoredData payload = TerrainControllerAttachment.StoredData.of(
                    Map.of(modifier, newState)
            );
            ClientPacketDistributor.sendToServer(new UpdateControllerPacket(controllerPos, payload));
            if (modifier.requireReRender()) {
                Minecraft.getInstance().levelRenderer.allChanged();
            }
        }

    }







}
