package com.arsmeteorites.arsmeteorites.common.recipe;

import com.arsmeteorites.arsmeteorites.common.recipe.builder.MeteoriteRegistryHelper;

import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class BasicFormula {

    public static void BasicFormulaRegistration() {
        MeteoriteRegistryHelper.registerMeteoriteType(
                Items.AIR, 1, 0,
                new Block[] {
                        Blocks.STONE, Blocks.DEEPSLATE, Blocks.ANDESITE, Blocks.DIORITE, Blocks.GRANITE,
                        Blocks.TUFF, Blocks.COBBLESTONE, Blocks.BLACKSTONE, Blocks.BASALT, Blocks.CALCITE,
                        Blocks.MAGMA_BLOCK, Blocks.IRON_ORE, Blocks.GOLD_ORE },
                new int[] {
                        200, 180, 150, 150, 150,
                        120, 100, 80, 60, 40,
                        20, 5, 1 });

        MeteoriteRegistryHelper.registerMeteoriteType(
                Items.PRISMARINE_SHARD, 5, 0,
                new Block[] {
                        Blocks.PRISMARINE, Blocks.PRISMARINE_BRICKS, Blocks.DARK_PRISMARINE,
                        Blocks.SEA_LANTERN, Blocks.SEA_PICKLE, Blocks.BUBBLE_CORAL_BLOCK,
                        Blocks.TUBE_CORAL_BLOCK, Blocks.BRAIN_CORAL_BLOCK, Blocks.FIRE_CORAL_BLOCK,
                        Blocks.BARREL, Blocks.CHISELED_STONE_BRICKS, Blocks.SPONGE
                },
                new int[] {
                        100, 50, 40,
                        30, 25, 25,
                        15, 15, 10,
                        3, 2, 1
                });

        MeteoriteRegistryHelper.registerMeteoriteType(
                Items.SAND, 5, 0,
                new Block[] {
                        Blocks.SANDSTONE, Blocks.CUT_SANDSTONE, Blocks.SMOOTH_SANDSTONE, Blocks.CHISELED_SANDSTONE,
                        Blocks.TERRACOTTA, Blocks.YELLOW_TERRACOTTA, Blocks.RED_TERRACOTTA, Blocks.WHITE_TERRACOTTA,
                        Blocks.CACTUS, Blocks.DEAD_BUSH, Blocks.SAND, Blocks.RED_SAND,
                        Blocks.SMOOTH_QUARTZ, Blocks.QUARTZ_PILLAR
                },
                new int[] {
                        120, 100, 110, 100,
                        80, 80, 60, 50,
                        20, 15, 15, 10,
                        2, 1
                });

        MeteoriteRegistryHelper.registerMeteoriteType(
                Items.ICE, 5, 0,
                new Block[] {
                        Blocks.ICE, Blocks.PACKED_ICE, Blocks.BLUE_ICE,
                        Blocks.SNOW_BLOCK, Blocks.POWDER_SNOW, Blocks.SNOW,
                        Blocks.CALCITE, Blocks.POINTED_DRIPSTONE,
                        Blocks.POLISHED_DIORITE, Blocks.QUARTZ_BLOCK
                },
                new int[] {
                        200, 150, 120,
                        100, 80, 50,
                        30, 20,
                        1, 1
                });

        MeteoriteRegistryHelper.registerMeteoriteType(
                Items.NETHER_BRICK, 10, 0,
                new Block[] {
                        Blocks.NETHERRACK, Blocks.BLACKSTONE, Blocks.BASALT, Blocks.MAGMA_BLOCK,
                        Blocks.SOUL_SAND, Blocks.SOUL_SOIL, Blocks.GLOWSTONE, Blocks.SHROOMLIGHT,
                        Blocks.NETHER_QUARTZ_ORE, Blocks.NETHER_GOLD_ORE, Blocks.GILDED_BLACKSTONE,
                        Blocks.CRACKED_NETHER_BRICKS, Blocks.CHISELED_NETHER_BRICKS, Blocks.RED_NETHER_BRICKS
                },
                new int[] {
                        200, 180, 150, 120,
                        50, 40, 30, 25,
                        20, 15, 15,
                        2, 1, 1
                });

        MeteoriteRegistryHelper.registerMeteoriteType(
                Items.ENDER_PEARL, 25, 0,
                new Block[] {
                        Blocks.END_STONE, Blocks.END_STONE_BRICKS, Blocks.PURPUR_BLOCK, Blocks.PURPUR_PILLAR,
                        Blocks.CHORUS_PLANT, Blocks.CHORUS_FLOWER,
                        Blocks.OBSIDIAN, Blocks.IRON_BARS, Blocks.END_ROD,
                        Blocks.DRAGON_HEAD, Blocks.PURPUR_SLAB
                },
                new int[] {
                        250, 200, 150, 120,
                        30, 80,
                        50, 40, 30,
                        1, 1,
                });

        MeteoriteRegistryHelper.registerMeteoriteType(
                Items.GLOW_BERRIES, 5, 0,
                new Block[] {
                        Blocks.MOSS_BLOCK, Blocks.MOSS_CARPET, Blocks.AZALEA, Blocks.FLOWERING_AZALEA,
                        Blocks.GLOW_LICHEN, Blocks.SPORE_BLOSSOM, Blocks.HANGING_ROOTS,
                        Blocks.CLAY, Blocks.DRIPSTONE_BLOCK, Blocks.POINTED_DRIPSTONE,
                        Blocks.COBBLED_DEEPSLATE, Blocks.ROOTED_DIRT, Blocks.SCULK
                },
                new int[] {
                        200, 10, 20, 20,
                        5, 5, 10,
                        150, 80, 60,
                        30, 25, 25
                });

        MeteoriteRegistryHelper.registerMeteoriteType(
                Items.RED_DYE, 2, 0,
                new Block[] {
                        Blocks.RED_TERRACOTTA, Blocks.ORANGE_TERRACOTTA, Blocks.YELLOW_TERRACOTTA,
                        Blocks.WHITE_TERRACOTTA, Blocks.BROWN_TERRACOTTA,
                        Blocks.TERRACOTTA, Blocks.RED_SAND, Blocks.RED_SANDSTONE,
                        Blocks.CACTUS, Blocks.DEAD_BUSH, Blocks.GOLD_ORE,
                        Blocks.RAIL, Blocks.CHISELED_SANDSTONE
                },
                new int[] {
                        180, 160, 140, 120, 100,
                        80, 60, 50,
                        40, 30, 20,
                        5, 5
                });

        MeteoriteRegistryHelper.registerMeteoriteType(
                Items.COAL, 20, 0,
                new Block[] {
                        Blocks.COAL_ORE, Blocks.DEEPSLATE_COAL_ORE, Blocks.COAL_BLOCK,
                        Blocks.STONE, Blocks.DEEPSLATE, Blocks.TUFF,
                        Blocks.ANDESITE, Blocks.GRAVEL,
                        Blocks.MOSSY_COBBLESTONE, Blocks.IRON_ORE
                },
                new int[] {
                        30, 25, 5,
                        20, 15, 10,
                        8, 5,
                        5, 3
                });

        MeteoriteRegistryHelper.registerMeteoriteType(
                Items.IRON_INGOT, 25, 0,
                new Block[] {
                        Blocks.IRON_ORE, Blocks.DEEPSLATE_IRON_ORE, Blocks.RAW_IRON_BLOCK,
                        Blocks.IRON_BLOCK,
                        Blocks.STONE, Blocks.DEEPSLATE, Blocks.TUFF,
                        Blocks.DIORITE, Blocks.GRANITE,
                        Blocks.COPPER_ORE, Blocks.LAPIS_ORE, Blocks.EMERALD_ORE
                },
                new int[] {
                        30, 25, 8, 5,
                        18, 15, 10,
                        8, 6,
                        4, 3, 2
                });

        MeteoriteRegistryHelper.registerMeteoriteType(
                Items.COPPER_INGOT, 20, 0,
                new Block[] {
                        Blocks.COPPER_ORE, Blocks.DEEPSLATE_COPPER_ORE,
                        Blocks.RAW_COPPER_BLOCK, Blocks.OXIDIZED_COPPER,
                        Blocks.DRIPSTONE_BLOCK, Blocks.POINTED_DRIPSTONE,
                        Blocks.CALCITE, Blocks.TUFF,
                        Blocks.WEATHERED_COPPER, Blocks.MOSS_BLOCK
                },
                new int[] {
                        35, 25, 10, 5,
                        15, 10,
                        8, 7,
                        5, 2
                });

        MeteoriteRegistryHelper.registerMeteoriteType(
                Items.GOLD_INGOT, 30, 0,
                new Block[] {
                        Blocks.GOLD_ORE, Blocks.DEEPSLATE_GOLD_ORE,
                        Blocks.RAW_GOLD_BLOCK, Blocks.GILDED_BLACKSTONE,
                        Blocks.NETHERRACK, Blocks.BLACKSTONE,
                        Blocks.BASALT, Blocks.MAGMA_BLOCK,
                        Blocks.NETHER_GOLD_ORE, Blocks.ANCIENT_DEBRIS
                },
                new int[] {
                        30, 20, 8, 5,
                        25, 15,
                        10, 5,
                        4, 2
                });

        MeteoriteRegistryHelper.registerMeteoriteType(
                Items.DIAMOND, 50, 0,
                new Block[] {
                        Blocks.DIAMOND_ORE, Blocks.DEEPSLATE_DIAMOND_ORE,
                        Blocks.DIAMOND_BLOCK,
                        Blocks.DEEPSLATE, Blocks.TUFF, Blocks.SCULK,
                        Blocks.OBSIDIAN, Blocks.BUDDING_AMETHYST
                },
                new int[] {
                        35, 30, 5,
                        25, 20, 10,
                        3, 1
                });

        MeteoriteRegistryHelper.registerMeteoriteType(
                Items.NETHERITE_SCRAP, 200, 0,
                new Block[] {
                        Blocks.ANCIENT_DEBRIS, Blocks.NETHERITE_BLOCK,
                        Blocks.BASALT, Blocks.BLACKSTONE,
                        Blocks.MAGMA_BLOCK, Blocks.SOUL_SAND,
                        Blocks.CRYING_OBSIDIAN, Blocks.LODESTONE
                },
                new int[] {
                        35, 5,
                        30, 25,
                        15, 10,
                        3, 2
                });

        MeteoriteRegistryHelper.registerMeteoriteType(
                Items.NETHER_STAR, 50, 1,
                new Block[] {
                        Blocks.NETHERITE_BLOCK, Blocks.EMERALD_BLOCK,
                        Blocks.DIAMOND_BLOCK, Blocks.GOLD_BLOCK,
                        Blocks.QUARTZ_BLOCK, Blocks.LAPIS_BLOCK,
                        Blocks.REDSTONE_BLOCK, Blocks.IRON_BLOCK,
                        Blocks.COAL_BLOCK
                },
                new int[] {
                        5, 5,
                        5, 5,
                        5, 5,
                        5, 5,
                        5
                });
    }
}
