// MeteoriteRegistryHelper.java
package com.arsmeteorites.arsmeteorites.common.recipe.builder;

import com.arsmeteorites.arsmeteorites.common.RecipeRegistry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public final class MeteoriteRegistryHelper {

    private static final Logger LOGGER = LogManager.getLogger();

    // 资源缓存减少注册表查询
    private static final Map<String, Item> ITEM_CACHE = new HashMap<>();
    private static final Map<String, Block> BLOCK_CACHE = new HashMap<>();

    public static void registerMeteoriteType(
                                             String inputItemId,
                                             int source,
                                             String[] blockIds,
                                             int[] weights) {
        String normalizedId = generateNormalizedId(inputItemId);

        Item input = resolveItem(inputItemId);
        if (input == null) {
            LOGGER.error("无法注册陨石类型 {}: 物品 {} 不存在", normalizedId, inputItemId);
            return;
        }

        Block[] blocks = new Block[blockIds.length];
        for (int i = 0; i < blockIds.length; i++) {
            blocks[i] = resolveBlock(blockIds[i]);
            if (blocks[i] == null) {
                LOGGER.error("无法注册陨石类型 {}: 方块 {} 不存在", normalizedId, blockIds[i]);
                return;
            }
        }

        if (weights.length != blockIds.length) {
            LOGGER.error("无法注册陨石类型 {}: 权重数组长度不匹配", normalizedId);
            return;
        }

        registerMeteoriteType(normalizedId, input, source, blocks, weights);
    }

    public static void registerMeteoriteType(
                                             String id,
                                             Item input,
                                             int source,
                                             Block[] meteorites,
                                             int[] weights) {
        int totalWeight = 0;
        for (int weight : weights) totalWeight += weight;

        try {
            RecipeRegistry.registerMeteoriteType(
                    new RecipeRegistry.MeteoriteType(id, input, source, meteorites, weights, totalWeight));
            LOGGER.info("成功注册陨石类型: {}", id);
        } catch (IllegalStateException e) {
            LOGGER.error("注册陨石类型失败: {}", e.getMessage());
        }
    }

    private static String generateNormalizedId(String itemId) {
        return itemId.replace(':', '-');
    }

    private static Item resolveItem(String itemId) {
        return ITEM_CACHE.computeIfAbsent(itemId, id -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(id)));
    }

    private static Block resolveBlock(String blockId) {
        return BLOCK_CACHE.computeIfAbsent(blockId, id -> ForgeRegistries.BLOCKS.getValue(new ResourceLocation(id)));
    }
}
