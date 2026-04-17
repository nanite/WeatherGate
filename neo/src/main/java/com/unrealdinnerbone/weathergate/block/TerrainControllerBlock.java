package com.unrealdinnerbone.weathergate.block;

import com.unrealdinnerbone.weathergate.WeatherGateRegistry;
import com.unrealdinnerbone.weathergate.level.attachments.TerrainControllerAttachment;
import com.unrealdinnerbone.weathergate.level.attachments.terrain.ControllerData;
import com.unrealdinnerbone.weathergate.network.packets.s2c.OpenTerrainControllerPacket;
import com.unrealdinnerbone.weathergate.server.TerrainManager;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.network.PacketDistributor;

public class TerrainControllerBlock extends Block{

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
//        if(level instanceof ServerLevel serverLevel) {
//            TerrainManager.addBlockPos(serverLevel, blockPos);
//        }
    }

    @Override
    protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
        TerrainManager.removeBlockPos(level, pos);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, LivingEntity livingEntity, ItemStack itemStack) {
        // Retrieve the data component attached to the placed block item
        ControllerData data = itemStack.get(WeatherGateRegistry.CONTROLLER_DATA.entryValue());
        if (data != null) {
            TerrainControllerAttachment attachment = TerrainControllerAttachment.getAttachment(level);
            attachment.setData(blockPos, data);
            attachment.save(level);
        }else {
            if(level instanceof ServerLevel serverLevel) {
                TerrainManager.addBlockPos(serverLevel, blockPos);
            }
        }
        super.setPlacedBy(level, blockPos, blockState, livingEntity, itemStack);
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader levelReader, BlockPos pos, BlockState state, boolean includeData, Player player) {
        ItemStack stack = super.getCloneItemStack(levelReader, pos, state, includeData, player);
        if (levelReader instanceof Level level) {
            TerrainControllerAttachment attachment = TerrainControllerAttachment.getAttachment(level);
            ControllerData data = attachment.getData(pos);
            if (data != null) {
                stack.set(WeatherGateRegistry.CONTROLLER_DATA.entryValue(), data);
            }
        }
        return stack;
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide()) {
            TerrainControllerAttachment attachment = TerrainControllerAttachment.getAttachment(level);
            ControllerData data = attachment.getData(pos);
            ItemStack stack = new ItemStack(WeatherGateRegistry.TERIANN_CONTROLLER_ITEM.entryValue());
            ItemEntity entity = new ItemEntity(level, (double)pos.getX() + (double)0.5F, (double)pos.getY() + (double)0.5F, (double)pos.getZ() + (double)0.5F, stack);
            entity.setDefaultPickUpDelay();
            level.addFreshEntity(entity);
            stack.set(WeatherGateRegistry.CONTROLLER_DATA.entryValue(), data);
            attachment.removeData(pos);
            attachment.save(level);
        }
        return super.playerWillDestroy(level, pos, state, player);
    }
}
