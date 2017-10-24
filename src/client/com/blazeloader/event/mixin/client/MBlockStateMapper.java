package com.blazeloader.event.mixin.client;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.statemap.BlockStateMapper;
import net.minecraft.client.renderer.block.statemap.IStateMapper;

@Mixin(BlockStateMapper.class)
public interface MBlockStateMapper {
	@Accessor("blockStateMap")
	public Map<Block, IStateMapper> getBlockStateMap();
}
