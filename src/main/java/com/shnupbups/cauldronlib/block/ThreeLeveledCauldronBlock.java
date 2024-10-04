package com.shnupbups.cauldronlib.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.AbstractCauldronBlock;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;

/**
 * Represents a cauldron that has 3 fluid levels, like vanilla Water Cauldrons.
 */
public class ThreeLeveledCauldronBlock extends AbstractLeveledCauldronBlock {
	public static final IntProperty LEVEL = Properties.LEVEL_3;

	private final MapCodec<ThreeLeveledCauldronBlock> codec = createCodec(settings -> new ThreeLeveledCauldronBlock(settings, behaviorMap));

	@Override
	protected MapCodec<? extends AbstractCauldronBlock> getCodec() {
		return codec;
	}

	public ThreeLeveledCauldronBlock(Settings settings, CauldronBehavior.CauldronBehaviorMap behaviorMap) {
		super(settings, behaviorMap);
    }

	@Override
	public IntProperty getLevelProperty() {
		return LEVEL;
	}

	@Override
	public int getMaxLevel() {
		return 3;
	}
}
