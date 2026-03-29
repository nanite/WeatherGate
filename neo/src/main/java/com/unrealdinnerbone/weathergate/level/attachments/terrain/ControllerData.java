package com.unrealdinnerbone.weathergate.level.attachments.terrain;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class ControllerData {

    public static final int DEFAULT_RANGE = 64;

    public static ControllerData defaultData() {
        return new ControllerData(DEFAULT_RANGE, StoredData.empty());
    }

    public static final Codec<ControllerData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("range").forGetter(ControllerData::getRange),
            StoredData.CODEC.fieldOf("data").forGetter(ControllerData::getStoredData)
    ).apply(instance, ControllerData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ControllerData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            ControllerData::getRange,
            StoredData.STREAM_CODEC,
            ControllerData::getStoredData,
            ControllerData::new
    );

    private int range;
    private final StoredData storedData;

    public ControllerData(int range, StoredData storedData) {
        this.range = range;
        this.storedData = storedData;
    }

    public StoredData getStoredData() {
        return storedData;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

}
