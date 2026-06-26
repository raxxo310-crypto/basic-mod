package com.raxxo310.basicmod.block.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import net.neoforged.neoforge.network.codec.StreamCodec;
import net.neoforged.neoforge.network.NetworkEvent;

public class ExampleBlockTeamConfigPayload {
    public static final StreamCodec<FriendlyByteBuf, ExampleBlockTeamConfigPayload> CODEC =
            StreamCodec.composite(
                    BlockPos.STREAM_CODEC,
                    p -> p.blockPos,
                    FriendlyByteBuf::readUtf,
                    p -> p.team,
                    ExampleBlockTeamConfigPayload::new
            );

    private final BlockPos blockPos;
    private final String team;

    public ExampleBlockTeamConfigPayload(BlockPos blockPos, String team) {
        this.blockPos = blockPos;
        this.team = team;
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }

    public String getTeam() {
        return team;
    }

    public static void handleData(ExampleBlockTeamConfigPayload data, PlayPayloadContext context) {
        context.enqueueWork(() -> {
            var player = context.player();
            if (player != null && player.hasPermissions(2)) { // Check if player is OP (permission level 2+)
                var level = player.serverLevel();
                if (level != null) {
                    var blockEntity = level.getBlockEntity(data.blockPos);
                    if (blockEntity instanceof com.raxxo310.basicmod.block.entity.ExampleBlockEntity example) {
                        example.setTeam(data.team);
                        level.sendBlockUpdated(data.blockPos, level.getBlockState(data.blockPos), level.getBlockState(data.blockPos), 3);
                    }
                }
            }
        }).exceptionally(e -> {
            context.disconnect(new RuntimeException("Failed to handle team config packet", e));
            return null;
        });
    }
}
