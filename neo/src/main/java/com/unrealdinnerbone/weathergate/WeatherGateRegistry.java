package com.unrealdinnerbone.weathergate;

import com.unrealdinnerbone.trenzalore.api.platform.services.ICreativeTabRegister;
import com.unrealdinnerbone.trenzalore.api.platform.services.IRegistry;
import com.unrealdinnerbone.trenzalore.api.registry.AbstractRegistryObjects;
import com.unrealdinnerbone.trenzalore.api.registry.BlockRegistryObjects;
import com.unrealdinnerbone.trenzalore.api.registry.ItemRegistryObjects;
import com.unrealdinnerbone.trenzalore.api.registry.Regeneration;
import com.unrealdinnerbone.trenzalore.api.registry.RegistryEntry;
import com.unrealdinnerbone.trenzalore.api.registry.RegistryObjects;
import com.unrealdinnerbone.trenzalore.lib.CreativeTabs;
import com.unrealdinnerbone.weathergate.block.TerrainControllerBlock;
import com.unrealdinnerbone.weathergate.level.attachments.SnowCatcherAttachment;
import com.unrealdinnerbone.weathergate.block.SnowCatcherBlock;
import com.unrealdinnerbone.weathergate.level.attachments.TerrainControllerAttachment;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
public class WeatherGateRegistry implements IRegistry {

    private static final BlockRegistryObjects BLOCKS = Regeneration.createBlockRegistry(WeatherGate.MOD_ID);
    private static final ItemRegistryObjects ITEMS = Regeneration.createItemRegistry(WeatherGate.MOD_ID);
    private static final RegistryObjects<AttachmentType<?>> ATTACHMENT_TYPE = Regeneration.create(WeatherGate.MOD_ID, NeoForgeRegistries.Keys.ATTACHMENT_TYPES);

    public static final RegistryEntry<AttachmentType<?>, AttachmentType<SnowCatcherAttachment>> SNOW_CATCHER_ATTACHMENT = ATTACHMENT_TYPE.register("snow_catcher", () ->
            AttachmentType.builder(() -> SnowCatcherAttachment.EMPTY).serialize(SnowCatcherAttachment.MAP_CODEC).sync(SnowCatcherAttachment.STREAM_CODEC).build());


    public static final RegistryEntry<AttachmentType<?>, AttachmentType<TerrainControllerAttachment>> TERIANN_CONTROLLER_ATTACHMENT = ATTACHMENT_TYPE.register("terrain_modifiers", () ->
            AttachmentType.builder(() -> TerrainControllerAttachment.EMPTY).serialize(TerrainControllerAttachment.MAP_CODEC).sync(TerrainControllerAttachment.STREAM_CODEC).build());

    public static final RegistryEntry.BlockEntry<SnowCatcherBlock> SNOW_CATCHER = BLOCKS.register("snow_catcher", SnowCatcherBlock::new, properties -> properties.mapColor(MapColor.SNOW).requiresCorrectToolForDrops().strength(5f).sound(SoundType.SNOW));
    public static final RegistryEntry.ItemEntry<BlockItem> SNOW_CATCHER_ITEM = ITEMS.registerBlockItem("snow_catcher", SNOW_CATCHER, properties -> properties);

    public static final RegistryEntry.BlockEntry<TerrainControllerBlock> TERIANN_CONTROLLER = BLOCKS.register("terrain_controller", TerrainControllerBlock::new, properties -> properties.mapColor(MapColor.STONE).strength(5.0F, 6.0F).sound(SoundType.STONE));
    public static final RegistryEntry.ItemEntry<BlockItem> TERIANN_CONTROLLER_ITEM = ITEMS.registerBlockItem("terrain_controller", TERIANN_CONTROLLER, properties -> properties);



    @Override
    public void afterRegistered(ICreativeTabRegister register) {
        register.addItemToCreativeTab(CreativeTabs.FUNCTIONAL_BLOCKS, List.of(SNOW_CATCHER_ITEM, TERIANN_CONTROLLER_ITEM));
    }

    @Override
    public List<AbstractRegistryObjects<?>> getRegistryObjects() {
        return List.of(BLOCKS, ITEMS,  ATTACHMENT_TYPE);
    }

    @Override
    public String getModID() {
        return WeatherGate.MOD_ID;
    }


}
