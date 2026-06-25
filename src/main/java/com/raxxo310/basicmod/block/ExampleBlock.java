package com.raxxo310.basicmod.block;

import com.raxxo310.basicmod.BasicMod;
import com.raxxo310.basicmod.block.entity.ExampleBlockEntity;
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
                example.damage(1, (ServerLevel) world, pos);
                player.sendMessage(Component.literal("Example Block health: " + example.getHealth()), player.getUUID());
                BasicMod.LOGGER.info("Player {} damaged example block at {} (health={})", player.getDisplayName().getString(), pos, example.getHealth());
            } else {
                player.sendMessage(Component.literal("This block has no health data."), player.getUUID());
            }
        }
        return InteractionResult.sidedSuccess(world.isClientSide);
    }

    @Override
    public void attack(BlockState state, Level world, BlockPos pos, Player player) {
        if (!world.isClientSide && world instanceof ServerLevel server) {
            BlockEntity be = world.getBlockEntity(pos);
            if (be instanceof ExampleBlockEntity example) {
                // Use the player's attack damage attribute, which includes weapon modifiers
                double attackDamage = player.getAttributeValue(Attributes.ATTACK_DAMAGE);
                int damageAmount = Math.max(1, (int) Math.round(attackDamage));
                example.damage(damageAmount, server, pos);
                player.sendMessage(Component.literal("You hit the block for " + damageAmount + " damage. Health: " + example.getHealth()), player.getUUID());
                BasicMod.LOGGER.info("Player {} attacked example block at {} for {} damage (health={})", player.getDisplayName().getString(), pos, damageAmount, example.getHealth());
            }
        }
        super.attack(state, world, pos, player);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ExampleBlockEntity(pos, state);
    }
}
