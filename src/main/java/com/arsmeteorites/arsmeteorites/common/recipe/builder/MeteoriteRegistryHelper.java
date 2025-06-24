package com.arsmeteorites.arsmeteorites.common.recipe.builder;

import com.arsmeteorites.arsmeteorites.ArsMeteorites;
import com.arsmeteorites.arsmeteorites.common.RecipeRegistry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.ForgeRegistries;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hollingsworth.arsnouveau.setup.registry.ItemsRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class MeteoriteRegistryHelper extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = new Gson();

    public MeteoriteRegistryHelper() {
        super(GSON, "meteorite_recipes");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> entries, @NotNull ResourceManager manager, @NotNull ProfilerFiller profiler) {
        ArsMeteorites.LOGGER.info("正在加载数据包陨石配方...");

        for (Map.Entry<ResourceLocation, JsonElement> entry : entries.entrySet()) {
            ResourceLocation recipeId = entry.getKey();
            JsonObject json = entry.getValue().getAsJsonObject();

            try {
                dataMeteoriteRecipe recipe = dataMeteoriteRecipe.fromJson(json);
                registerMeteoriteType(
                        recipe.inputItemId(),
                        recipe.source(),
                        recipe.model(),
                        recipe.catalysts(),
                        recipe.meteoriteBlockIds(),
                        recipe.weights());
            } catch (Exception e) {
                ArsMeteorites.LOGGER.error("加载配方 {} 失败", recipeId);
            }
        }

        ArsMeteorites.LOGGER.info("已加载 {} 个陨石配方", entries.size());
    }

    public static void registerMeteoriteType(String inputItemId, double source, int model, String catalyst, String[] blockIds, int[] weights) {
        String normalizedId = generateNormalizedId(inputItemId);

        Item input = ArsMeteorites.getItem(inputItemId);
        if (input == null) {
            ArsMeteorites.LOGGER.error("无法注册陨石类型 {}: 物品 {} 不存在", normalizedId, inputItemId);
            return;
        }

        Block[] blocks = new Block[blockIds.length];
        for (int i = 0; i < blockIds.length; i++) {
            blocks[i] = ArsMeteorites.getBlock(blockIds[i]);
            if (blocks[i] == Blocks.BARRIER) {
                ArsMeteorites.LOGGER.error("无法注册陨石类型 {}: 方块 {} 不存在", normalizedId, blockIds[i]);
                return;
            }
        }

        Item catalysts = ArsMeteorites.getItem(catalyst);
        if (catalysts == null) {
            ArsMeteorites.LOGGER.error("无法注册陨石类型 {}: 催化剂 {} 不存在", normalizedId, catalyst);
            return;
        }

        registerMeteoriteType(normalizedId, input, source, model, catalysts, blocks, weights);
    }

    public static void registerMeteoriteType(Item input, double source, int model, Block[] meteorites, int[] weights) {
        Item catalysts = ItemsRegistry.SOURCE_GEM.get().asItem();
        String normalizedId = generateNormalizedId(input);
        registerMeteoriteType(normalizedId, input, source, model, catalysts, meteorites, weights);
    }

    public static void registerMeteoriteType(String id, Item input, double source, int model, Item catalysts, Block[] meteorites, int[] weights) {
        if (weights.length != meteorites.length) {
            ArsMeteorites.LOGGER.error("无法注册陨石类型 {}: 权重数组长度不匹配", id);
            return;
        }

        int totalWeight = 0;
        for (int weight : weights) totalWeight += weight;

        try {
            RecipeRegistry.registerMeteoriteType(
                    new RecipeRegistry.MeteoriteType(id, input, source, model, catalysts, meteorites, weights, totalWeight));
        } catch (IllegalStateException e) {
            ArsMeteorites.LOGGER.error("注册陨石类型失败: {}", id);
        }
    }

    public static String generateNormalizedId(String itemId) {
        return itemId.replace(':', '-');
    }

    public static String generateNormalizedId(Item item) {
        if (item != null) {
            ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(item);
            return itemId.getNamespace() + "-" + itemId.getPath();
        } else {
            return "default";
        }
    }
}
