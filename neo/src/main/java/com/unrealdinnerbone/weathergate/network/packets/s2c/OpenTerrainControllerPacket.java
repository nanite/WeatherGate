package com.unrealdinnerbone.weathergate.network.packets.s2c;

import com.unrealdinnerbone.weathergate.WeatherGate;
import com.unrealdinnerbone.weathergate.client.screen.TerrainControllerScreen2;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;


import java.util.Map;

public record OpenTerrainControllerPacket(BlockPos blockPos) implements CustomPacketPayload {

    public static final Type<OpenTerrainControllerPacket> TYPE = new CustomPacketPayload.Type<>(WeatherGate.id("open_terrain_controller_packet"));


    public static final StreamCodec<FriendlyByteBuf, OpenTerrainControllerPacket> CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, OpenTerrainControllerPacket::blockPos,
            OpenTerrainControllerPacket::new);


    public static void handleOpenTerrainControllerPacket(OpenTerrainControllerPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> new TerrainControllerScreen2(packet.blockPos()).openGui());
    }


    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
