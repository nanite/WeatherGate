package com.unrealdinnerbone.weathergate.block;

import com.unrealdinnerbone.weathergate.level.attachments.TerrainControllerAttachment;
import com.unrealdinnerbone.weathergate.network.packets.s2c.OpenTerrainControllerPacket;
import com.unrealdinnerbone.weathergate.server.TerrainManager;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Map;

public class TerrainControllerBlock extends Block{

    public static final int RANGE = 64;

    public TerrainControllerBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }
//

    @Override
    public InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult hitResult) {
        if(!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
            PacketDistributor.sendToPlayer(serverPlayer, new OpenTerrainControllerPacket(blockPos));
        }
        return InteractionResult.SUCCESS_SERVER;
    }

    @Override
    public void onPlace(BlockState newState, Level level, BlockPos blockPos, BlockState oldState, boolean pistonMoved) {
        if(level instanceof ServerLevel serverLevel) {
            TerrainManager.addBlockPos(serverLevel, blockPos);
        }
    }

    @Override
    protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
        TerrainManager.removeBlockPos(level, pos);
    }
}
