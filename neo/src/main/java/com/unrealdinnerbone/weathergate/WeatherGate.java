package com.unrealdinnerbone.weathergate;

import com.unrealdinnerbone.trenzalore.lib.IDUtils;
import com.unrealdinnerbone.weathergate.client.WeatherGateClient;
import net.minecraft.resources.Identifier;
import com.unrealdinnerbone.weathergate.data.WeatherGateData;
import com.unrealdinnerbone.weathergate.network.WeatherGateNetwork;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@Mod(WeatherGate.MOD_ID)
public class WeatherGate {
    public static final String MOD_ID = "weathergate";


    public WeatherGate(IEventBus eventBus, Dist dist) {
        eventBus.addListener(WeatherGateData::onData);
        eventBus.addListener(WeatherGateNetwork::register);
        if(dist == Dist.CLIENT) {
            WeatherGateClient.init(eventBus);
        }
    }


    public static void onServer(RegisterCommandsEvent event) {
    }

    public static Identifier id(String editNbt) {
        return IDUtils.id(MOD_ID, editNbt);
    }
}