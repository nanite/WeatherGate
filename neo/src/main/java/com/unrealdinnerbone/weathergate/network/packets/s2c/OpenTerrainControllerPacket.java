package com.unrealdinnerbone.weathergate.network.packets.s2c;

import com.mojang.logging.LogUtils;
import com.unrealdinnerbone.weathergate.WeatherGate;
import com.unrealdinnerbone.weathergate.client.screen.TerrainControllerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.slf4j.Logger;

public record OpenTerrainControllerPacket(BlockPos blockPos) implements CustomPacketPayload {

    private static final Logger LOGGER = LogUtils.getLogger();
    public static final Type<OpenTerrainControllerPacket> TYPE = new CustomPacketPayload.Type<>(WeatherGate.id("open_terrain_controller_packet"));


    public static final StreamCodec<FriendlyByteBuf, OpenTerrainControllerPacket> CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, OpenTerrainControllerPacket::blockPos,
            OpenTerrainControllerPacket::new);


    public static void handleOpenTerrainControllerPacket(OpenTerrainControllerPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            try {
                new TerrainControllerScreen(packet.blockPos()).openGui();
            } catch (Exception e) {
                context.player().sendSystemMessage(Component.literal("Error opening gui please see console"));
                LOGGER.error("Failed to open terrain controller screen for block at {}", packet.blockPos(), e);
            }
        });
    }


    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
