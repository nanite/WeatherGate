package com.unrealdinnerbone.weathergate.level.attachments;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.unrealdinnerbone.weathergate.WeatherGateRegistry;
import com.unrealdinnerbone.weathergate.util.RangeUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public record SnowCatcherAttachment(List<BlockPos> blockPosList) {

    public static SnowCatcherAttachment of(List<BlockPos> data) {
        return new SnowCatcherAttachment(new ArrayList<>(data));
    }

    public static final SnowCatcherAttachment EMPTY = new SnowCatcherAttachment(new ArrayList<>());

    public static final MapCodec<SnowCatcherAttachment> MAP_CODEC = RecordCodecBuilder.mapCodec(
            i -> i
                    .group(BlockPos.CODEC.listOf().fieldOf("blockPos").forGetter(o -> o.blockPosList))
                    .apply(i, SnowCatcherAttachment::of)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, SnowCatcherAttachment> STREAM_CODEC =
            StreamCodec.composite(
                    BlockPos.STREAM_CODEC.apply(ByteBufCodecs.list()),
                    SnowCatcherAttachment::blockPosList,
                    SnowCatcherAttachment::of
            );


    public static SnowCatcherAttachment get(Level level) {
        return level.getData(WeatherGateRegistry.SNOW_CATCHER_ATTACHMENT.entryValue());
    }

    public void save(Level level) {
        level.setData(WeatherGateRegistry.SNOW_CATCHER_ATTACHMENT.entryValue(), this);
    }

    public static void addBlockPos(Level level, BlockPos blockPos) {
        SnowCatcherAttachment snowCatcherAttachment = get(level);
        snowCatcherAttachment.blockPosList.add(blockPos);
        snowCatcherAttachment.save(level);
    }

    public static void removeBlockPos(Level level, BlockPos blockPos) {
        SnowCatcherAttachment snowCatcherAttachment = get(level);
        snowCatcherAttachment.blockPosList.remove(blockPos);
        snowCatcherAttachment.save(level);
    }

    public static boolean isInRange(Level level, Vec3i blockPos) {
        for (BlockPos location : get(level).blockPosList()) {
            if(RangeUtils.isWithinRange(location, blockPos, 64)) {
                return true;
            }
        }
        return false;
    }
}
