package com.unrealdinnerbone.weathergate.network.packets.c2s;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import com.unrealdinnerbone.weathergate.WeatherGate;
import com.unrealdinnerbone.weathergate.level.attachments.TerrainControllerAttachment;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;

import java.util.HashMap;
import java.util.Map;

public record UpdateControllerPacket(
        GlobalPos globalPos,
        TerrainControllerAttachment.StoredData updateMap
) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<UpdateControllerPacket> TYPE =
            new CustomPacketPayload.Type<>(WeatherGate.id("update_controller_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateControllerPacket> CODEC = StreamCodec.composite(
            GlobalPos.STREAM_CODEC, UpdateControllerPacket::globalPos,
            TerrainControllerAttachment.StoredData.STREAM_CODEC, UpdateControllerPacket::updateMap,
            UpdateControllerPacket::new
    );

    public static void handleUpdateColorPacket(UpdateControllerPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (!(context.player().level() instanceof ServerLevel serverLevel)) {
                return;
            }
            TerrainControllerAttachment attachment = TerrainControllerAttachment.getAttachment(serverLevel);
            TerrainControllerAttachment.StoredData map = attachment.data().computeIfAbsent(packet.globalPos().pos(), ignored -> new TerrainControllerAttachment.StoredData(new HashMap<>()));
            map.modifiers().putAll(packet.updateMap.modifiers());
            attachment.save(serverLevel);
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}