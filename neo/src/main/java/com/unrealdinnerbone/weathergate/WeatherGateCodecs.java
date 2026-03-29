package com.unrealdinnerbone.weathergate;

import com.mojang.serialization.Codec;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class WeatherGateCodecs
{
    public static final Codec<Color4I> COLOR4I_CODEC = Codec.INT.xmap(Color4I::rgb, Color4I::rgb);
    public static final Codec<Color4I> COLOR4I_ALPAH_CODEC = Codec.INT.xmap(Color4I::rgba, Color4I::rgba);
    public static final StreamCodec<RegistryFriendlyByteBuf, Color4I> COLOR4I_STREAM_CODEC = StreamCodec.of((byteBuf, color4I) -> byteBuf.writeInt(color4I.rgb()), byteBuf -> Color4I.rgb(byteBuf.readInt()));
    public static final StreamCodec<RegistryFriendlyByteBuf, Color4I> COLOR4I_ALPHA_STREAM_CODEC = StreamCodec.of((byteBuf, color4I) -> byteBuf.writeInt(color4I.rgba()), byteBuf -> Color4I.rgba(byteBuf.readInt()));

}
