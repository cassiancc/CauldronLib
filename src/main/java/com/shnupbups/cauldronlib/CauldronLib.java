package com.shnupbups.cauldronlib;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvents;

public class CauldronLib {
	private static final Set<Map<Item, CauldronBehavior>> CAULDRON_BEHAVIOR_MAPS = new HashSet<>(Set.of(
			CauldronBehavior.EMPTY_CAULDRON_BEHAVIOR,
			CauldronBehavior.WATER_CAULDRON_BEHAVIOR,
			CauldronBehavior.LAVA_CAULDRON_BEHAVIOR,
			CauldronBehavior.POWDER_SNOW_CAULDRON_BEHAVIOR
	));

	private static final Set<CauldronBehaviorMapEntry> GLOBAL_BEHAVIORS = new HashSet<>();

	/**
	 * Registers a cauldron behavior map.
	 *
	 * @param behaviorMap the behavior map to register
	 */
	public static void registerBehaviorMap(Map<Item, CauldronBehavior> behaviorMap) {
		CAULDRON_BEHAVIOR_MAPS.add(behaviorMap);
		addGlobalBehaviors(behaviorMap);
	}

	/**
	 * Registers cauldron behavior maps.
	 *
	 * @param behaviorMaps the behavior maps to register
	 */
	public static void registerBehaviorMaps(Map<Item, CauldronBehavior>... behaviorMaps) {
		Arrays.stream(behaviorMaps).forEach(CauldronLib::registerBehaviorMap);
	}

	/**
	 * Gets all registered cauldron behavior maps.
	 */
	public static Set<Map<Item, CauldronBehavior>> getCauldronBehaviorMaps() {
		return CAULDRON_BEHAVIOR_MAPS;
	}

	/**
	 * Gets all registered global cauldron behaviors.
	 */
	public static Set<CauldronBehaviorMapEntry> getGlobalBehaviors() {
		return GLOBAL_BEHAVIORS;
	}

	/**
	 * Adds all currently registered global behaviors to the given behavior map.
	 *
	 * @param behaviorMap the behavior map to add to
	 */
	private static void addGlobalBehaviors(Map<Item, CauldronBehavior> behaviorMap) {
		CauldronBehavior.registerBucketBehavior(behaviorMap);
		getGlobalBehaviors().forEach((behavior) -> {
			behaviorMap.put(behavior.item(), behavior.behavior());
		});
	}

	/**
	 * Registers a new global cauldron behavior.
	 * <p>Global cauldron behaviors are added to every registered cauldron behavior map.
	 */
	public static void registerGlobalBehavior(Item item, CauldronBehavior behavior) {
		registerGlobalBehavior(new CauldronBehaviorMapEntry(item, behavior));
	}

	/**
	 * Registers new global cauldron behaviors.
	 * <p>Global cauldron behaviors are added to every registered cauldron behavior map.
	 */
	public static void registerGlobalBehavior(CauldronBehaviorMapEntry... behaviors) {
		Arrays.stream(behaviors).forEach((behavior -> {
			getCauldronBehaviorMaps().forEach((map) -> map.put(behavior.item(), behavior.behavior));
			getGlobalBehaviors().add(behavior);
		}));
	}

	/**
	 * Creates a new cauldron behavior for filling a cauldron from a bucket.
	 *
	 * @param cauldron the filled cauldron that results
	 */
	public static CauldronBehavior createFillCauldronBehavior(Block cauldron) {
		return (state, world, pos, player, hand, stack) -> CauldronBehavior.fillCauldron(world, pos, player, hand, stack, cauldron.getDefaultState(), SoundEvents.ITEM_BUCKET_EMPTY);
	}

	/**
	 * Registers a new global cauldron behavior for filling a cauldron from a bucket.
	 *
	 * @param bucket   the bucket to fill from
	 * @param cauldron the filled cauldron that results
	 */
	public static void registerFillCauldronBehavior(Item bucket, Block cauldron) {
		registerGlobalBehavior(bucket, createFillCauldronBehavior(cauldron));
	}

	/**
	 * Creates a new cauldron behavior for emptying a cauldron into a bucket.
	 *
	 * @param bucket the bucket that results
	 */
	public static CauldronBehavior createEmptyCauldronBehavior(Item bucket) {
		return (state2, world, pos, player, hand, stack) -> CauldronBehavior.emptyCauldron(state2, world, pos, player, hand, stack, new ItemStack(bucket), state -> true, SoundEvents.ITEM_BUCKET_FILL);
	}

	/**
	 * A pair of an item and a cauldron behavior
	 */
	public record CauldronBehaviorMapEntry(Item item, CauldronBehavior behavior) {
	}
}
