package com.unrealdinnerbone.weathergate.network;

import com.unrealdinnerbone.weathergate.WeatherGate;
import com.unrealdinnerbone.weathergate.network.packets.c2s.UpdateControllerPacket;
import com.unrealdinnerbone.weathergate.network.packets.s2c.OpenTerrainControllerPacket;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class WeatherGateNetwork {

    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(WeatherGate.MOD_ID);


        registrar.playToClient(OpenTerrainControllerPacket.TYPE, OpenTerrainControllerPacket.CODEC, OpenTerrainControllerPacket::handleOpenTerrainControllerPacket);

        registrar.playToServer(UpdateControllerPacket.TYPE, UpdateControllerPacket.CODEC, UpdateControllerPacket::handleUpdateColorPacket);




    }
}
