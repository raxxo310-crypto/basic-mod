package com.raxxo310.basicmod;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod("basicmod")
public class BasicMod {
    public static final String MOD_ID = "basicmod";
    private static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    // A simple example block. In a real mod you would use the game's registry
    // systems (DeferredRegister / RegistryEvents) to register blocks and their
    // corresponding block items. This keeps the example minimal and focused.
    public static final Block EXAMPLE_BLOCK = new Block(BlockBehaviour.Properties.of(Material.STONE).strength(3.0f));

    public BasicMod(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        NeoForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Register the example block so it has a registry name and is available in-game.
        Registry.register(Registry.BLOCK, new ResourceLocation(MOD_ID, "example_block"), EXAMPLE_BLOCK);

        LOGGER.info("Basic Mod initialized!");
        LOGGER.info("Registered example block: {}", EXAMPLE_BLOCK);
    }
}
