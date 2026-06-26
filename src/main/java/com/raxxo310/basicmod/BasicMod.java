package com.raxxo310.basicmod;

import com.raxxo310.basicmod.block.ExampleBlock;
import com.raxxo310.basicmod.block.entity.ExampleBlockEntity;
import com.raxxo310.basicmod.block.network.ExampleBlockTeamConfigPayload;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistration;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.network.SimpleChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod("basicmod")
public class BasicMod {
    public static final String MOD_ID = "basicmod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    // Example block (Backed by a BlockEntity to store health)
    public static final Block EXAMPLE_BLOCK = new ExampleBlock(BlockBehaviour.Properties.of(Material.STONE).strength(3.0f).randomTicks());

    // Block entity type will be registered during setup
    public static BlockEntityType<ExampleBlockEntity> EXAMPLE_BLOCK_ENTITY_TYPE;

    // Network channel for team config packets
    public static final SimpleChannel TEAM_CONFIG_PACKET = net.neoforged.neoforge.network.NetworkHooks.createPlayChannel(
            new ResourceLocation(MOD_ID, "team_config"),
            () -> "1.0",
            s -> true,
            s -> true
    );

    public BasicMod(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::registerPayloads);
        NeoForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Register the example block so it has a registry name and is available in-game.
        Registry.register(Registry.BLOCK, new ResourceLocation(MOD_ID, "example_block"), EXAMPLE_BLOCK);
        // Register a block item so players can obtain it
        Registry.register(Registry.ITEM, new ResourceLocation(MOD_ID, "example_block"), new BlockItem(EXAMPLE_BLOCK, new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));

        // Register the block entity type for the example block
        EXAMPLE_BLOCK_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE, new ResourceLocation(MOD_ID, "example_block_entity"), BlockEntityType.Builder.of(ExampleBlockEntity::new, EXAMPLE_BLOCK).build(null));

        LOGGER.info("Basic Mod initialized!");
        LOGGER.info("Registered example block: {}", EXAMPLE_BLOCK);
    }

    private void registerPayloads(final RegisterPayloadHandlersEvent event) {
        var registrations = event.registrar(MOD_ID);
        registrations.playToServer(
                ExampleBlockTeamConfigPayload.CODEC,
                ExampleBlockTeamConfigPayload::handleData
        );
    }
}
