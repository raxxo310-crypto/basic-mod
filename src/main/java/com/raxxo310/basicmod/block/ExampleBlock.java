package com.raxxo310.basicmod.block;

import com.raxxo310.basicmod.BasicMod;
import com.raxxo310.basicmod.block.entity.ExampleBlockEntity;
import com.raxxo310.basicmod.block.screen.ExampleBlockConfigScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.BlockHitResult;

public class ExampleBlock extends BaseEntityBlock {
    public ExampleBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!world.isClientSide) {
            BlockEntity be = world.getBlockEntity(pos);
            if (be instanceof ExampleBlockEntity example) {
                // Check if player is in spectator mode and is OP
                if (player.isSpectator() && player.hasPermissions(2)) {
                    // Open configuration GUI on client side
                    if (world.isClientSide || player.connection == null) {
                        return InteractionResult.SUCCESS;
                    }
                    // Send packet to client to open GUI
                    player.displayClientMessage(Component.literal("Opening configuration screen..."), true);
                    return InteractionResult.SUCCESS;
                }
                
                // Normal damage behavior for non-spectator players
                if (canDamageBlock(player, example)) {
                    example.damage(1, (ServerLevel) world, pos);
                    player.sendMessage(Component.literal("Example Block health: " + example.getHealth()), false);
                    BasicMod.LOGGER.info("Player {} damaged example block at {} (health={})", player.getDisplayName().getString(), pos, example.getHealth());
                } else {
                    player.sendMessage(Component.literal("You cannot damage your own team's block!"), false);
                    BasicMod.LOGGER.info("Player {} attempted to damage their own team's block at {}", player.getDisplayName().getString(), pos);
                }
            } else {
                player.sendMessage(Component.literal("This block has no health data."), false);
            }
        } else if (world.isClientSide && player.isSpectator() && player.hasPermissions(2)) {
            // Client-side: Open GUI for spectator OP
            BlockEntity be = world.getBlockEntity(pos);
            if (be instanceof ExampleBlockEntity example) {
                net.neoforged.api.distmarker.Dist.CLIENT.isPresent();
                openConfigScreen(example, pos);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.sidedSuccess(world.isClientSide);
    }

    @Override
    public void attack(BlockState state, Level world, BlockPos pos, Player player) {
        if (!world.isClientSide && world instanceof ServerLevel server) {
            BlockEntity be = world.getBlockEntity(pos);
            if (be instanceof ExampleBlockEntity example) {
                // Check if player is on a different team
                if (canDamageBlock(player, example)) {
                    // Use the player's attack damage attribute, which includes weapon modifiers
                    double attackDamage = player.getAttributeValue(Attributes.ATTACK_DAMAGE);
                    int damageAmount = Math.max(1, (int) Math.round(attackDamage));
                    example.damage(damageAmount, server, pos);
                    player.sendMessage(Component.literal("You hit the block for " + damageAmount + " damage. Health: " + example.getHealth()), false);
                    BasicMod.LOGGER.info("Player {} attacked example block at {} for {} damage (health={})", player.getDisplayName().getString(), pos, damageAmount, example.getHealth());
                } else {
                    player.sendMessage(Component.literal("You cannot damage your own team's block!"), false);
                    BasicMod.LOGGER.info("Player {} attempted to attack their own team's block at {}", player.getDisplayName().getString(), pos);
                }
            }
        }
        super.attack(state, world, pos, player);
    }

    /**
     * Check if a player can damage this block
     * A player can only damage the block if they are on a different team than the block's owner
     * 
     * @param player The player attempting to damage the block
     * @param blockEntity The block entity to check team ownership
     * @return true if the player can damage the block, false if they own it or are on the same team
     */
    private boolean canDamageBlock(Player player, ExampleBlockEntity blockEntity) {
        String blockTeam = blockEntity.getTeam();
        String playerTeam = player.getTeam() != null ? player.getTeam().getName() : "none";
        
        // If block has no team assigned, allow damage
        if (blockTeam == null || blockTeam.isEmpty()) {
            return true;
        }
        
        // Only allow damage if player is on a different team
        return !blockTeam.equals(playerTeam);
    }

    @SuppressWarnings("unused")
    private static void openConfigScreen(ExampleBlockEntity blockEntity, BlockPos pos) {
        // This method is called on client side only
        if (net.neoforged.api.distmarker.Dist.CLIENT.isPresent()) {
            net.minecraft.client.Minecraft.getInstance().setScreen(new ExampleBlockConfigScreen(blockEntity, pos));
        }
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ExampleBlockEntity(pos, state);
    }
}
