package com.raxxo310.basicmod.util;

import net.minecraft.resources.ResourceLocation;

public class BasicModTags {
    public static class BlockTags {
        // Aeronautics compatibility tags
        public static final net.minecraft.tags.TagKey<net.minecraft.world.level.block.Block> AIRTIGHT = 
            net.minecraft.tags.BlockTags.create(new ResourceLocation("aeronautics", "airtight"));
        
        public static final net.minecraft.tags.TagKey<net.minecraft.world.level.block.Block> ENVELOPE = 
            net.minecraft.tags.BlockTags.create(new ResourceLocation("aeronautics", "envelope"));
        
        public static final net.minecraft.tags.TagKey<net.minecraft.world.level.block.Block> LEVITITE_CATALYZER = 
            net.minecraft.tags.BlockTags.create(new ResourceLocation("aeronautics", "levitite_catalyzer"));
    }
}
