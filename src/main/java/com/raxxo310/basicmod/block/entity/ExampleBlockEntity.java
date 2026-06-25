package com.raxxo310.basicmod.block.entity;

import com.raxxo310.basicmod.BasicMod;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ExampleBlockEntity extends BlockEntity {
    private int health = 10; // default health

    public ExampleBlockEntity(BlockPos pos, BlockState state) {
        super(BasicMod.EXAMPLE_BLOCK_ENTITY_TYPE, pos, state);
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
        setChanged();
    }

    public void damage(int amount, ServerLevel level, BlockPos pos) {
        this.health -= amount;
        setChanged();
        if (this.health <= 0) {
            // break the block
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            // optional: spawn particles or drop items here
            BasicMod.LOGGER.info("Example block at {} destroyed (health reached 0)", pos);
        } else {
            // update block state so clients re-request block entity data if needed
            level.sendBlockUpdated(pos, level.getBlockState(pos), level.getBlockState(pos), 3);
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("Health")) {
            this.health = tag.getInt("Health");
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("Health", this.health);
    }
}
