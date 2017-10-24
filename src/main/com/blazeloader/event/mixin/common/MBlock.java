package com.blazeloader.event.mixin.common;

import org.spongepowered.asm.mixin.Mixin;

import com.blazeloader.api.block.IBlock;

import net.minecraft.block.Block;

@Mixin(Block.class)
public interface MBlock extends IBlock {

}
