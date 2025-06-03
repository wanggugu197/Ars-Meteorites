package com.arsmeteorites.arsmeteorites.common.recipe.builder;

import com.arsmeteorites.arsmeteorites.common.ConjureMeteoritesRitual;
import com.arsmeteorites.arsmeteorites.common.RecipeRegistry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class MeteoriteRegistryHelper {

    private static final Logger LOGGER = LogManager.getLogger();

    private MeteoriteRegistryHelper() {}

    public static boolean registerMeteoriteType(
                                                String id,
                                                String inputItemId,
                                                String[] blockIds,
                                                int[] weights) {
        Item inputItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(inputItemId));
        if (inputItem == null) {
            LOGGER.error("Failed to register meteorite type {}: Item {} not found!", id, inputItemId);
            return false;
        }

        Block[] blocks = new Block[blockIds.length];
        for (int i = 0; i < blockIds.length; i++) {
            Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(blockIds[i]));
            if (block == null) {
                LOGGER.error("Failed to register meteorite type {}: Block {} not found!", id, blockIds[i]);
                return false;
            }
            blocks[i] = block;
        }

        if (weights.length != blockIds.length) {
            LOGGER.error("Failed to register meteorite type {}: Probability array length mismatch!", id);
            return false;
        }

        RecipeRegistry.registerMeteoriteType(new ConjureMeteoritesRitual.MeteoritesList(id, inputItem, blocks, weights));
        return true;
    }
}
