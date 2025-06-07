package com.arsmeteorites.arsmeteorites.common.recipe;

import com.arsmeteorites.arsmeteorites.common.recipe.builder.MeteoriteRegistryHelper;

import static com.arsmeteorites.arsmeteorites.ArsMeteorites.isModLoaded;

public class LinkageFormula {

    public static void LinkageFormulaRegistration() {
        MeteoriteRegistryHelper.registerMeteoriteType(
                "ars_nouveau:archwood_planks",
                50,
                "ars_nouveau:sourcestone",
                new String[] {
                        "ars_nouveau:archwood_planks",

                        "ars_nouveau:blue_archwood_leaves",
                        "ars_nouveau:blue_archwood_log",
                        "ars_nouveau:blue_archwood_wood",
                        "ars_nouveau:stripped_blue_archwood_log",
                        "ars_nouveau:stripped_blue_archwood_wood",

                        "ars_nouveau:green_archwood_leaves",
                        "ars_nouveau:green_archwood_log",
                        "ars_nouveau:green_archwood_wood",
                        "ars_nouveau:stripped_green_archwood_log",
                        "ars_nouveau:stripped_green_archwood_wood",

                        "ars_nouveau:purple_archwood_leaves",
                        "ars_nouveau:purple_archwood_log",
                        "ars_nouveau:purple_archwood_wood",
                        "ars_nouveau:stripped_purple_archwood_log",
                        "ars_nouveau:stripped_purple_archwood_wood",

                        "ars_nouveau:red_archwood_leaves",
                        "ars_nouveau:red_archwood_log",
                        "ars_nouveau:red_archwood_wood",
                        "ars_nouveau:stripped_red_archwood_log",
                        "ars_nouveau:stripped_red_archwood_wood",
                },
                new int[] { 120, 20, 80, 40, 5, 5, 20, 80, 40, 5, 5, 20, 80, 40, 5, 5, 20, 80, 40, 5, 5, });

        MeteoriteRegistryHelper.registerMeteoriteType(
                "ars_nouveau:sourcestone",
                200,
                "ars_nouveau:source_gem",
                new String[] {
                        "ars_nouveau:sourcestone",
                        "ars_nouveau:sourcestone_alternating",
                        "ars_nouveau:sourcestone_basketweave",
                        "ars_nouveau:sourcestone_large_bricks",
                        "ars_nouveau:sourcestone_mosaic",
                        "ars_nouveau:sourcestone_small_bricks",

                        "ars_nouveau:smooth_sourcestone",
                        "ars_nouveau:smooth_sourcestone_alternating",
                        "ars_nouveau:smooth_sourcestone_basketweave",
                        "ars_nouveau:smooth_sourcestone_large_bricks",
                        "ars_nouveau:smooth_sourcestone_mosaic",
                        "ars_nouveau:smooth_sourcestone_small_bricks",

                        "ars_nouveau:gilded_sourcestone_alternating",
                        "ars_nouveau:gilded_sourcestone_basketweave",
                        "ars_nouveau:gilded_sourcestone_large_bricks",
                        "ars_nouveau:gilded_sourcestone_mosaic",
                        "ars_nouveau:gilded_sourcestone_small_bricks",

                        "ars_nouveau:smooth_gilded_sourcestone_alternating",
                        "ars_nouveau:smooth_gilded_sourcestone_basketweave",
                        "ars_nouveau:smooth_gilded_sourcestone_large_bricks",
                        "ars_nouveau:smooth_gilded_sourcestone_mosaic",
                        "ars_nouveau:smooth_gilded_sourcestone_small_bricks",
                },
                new int[] { 50, 50, 50, 50, 50, 50, 40, 40, 40, 40, 40, 40, 30, 30, 30, 30, 30, 20, 20, 20, 20, 20 });

        if (isModLoaded("botania")) {
            MeteoriteRegistryHelper.registerMeteoriteType(
                    "botania:fertilizer",
                    50,
                    "ars_nouveau:source_gem",
                    new String[] {
                            "botania:white_petal_block",
                            "botania:orange_petal_block",
                            "botania:magenta_petal_block",
                            "botania:light_blue_petal_block",

                            "botania:yellow_petal_block",
                            "botania:lime_petal_block",
                            "botania:pink_petal_block",
                            "botania:gray_petal_block",

                            "botania:light_gray_petal_block",
                            "botania:cyan_petal_block",
                            "botania:purple_petal_block",
                            "botania:blue_petal_block",

                            "botania:brown_petal_block",
                            "botania:green_petal_block",
                            "botania:red_petal_block",
                            "botania:black_petal_block",
                    },
                    new int[] { 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, });

            MeteoriteRegistryHelper.registerMeteoriteType(
                    "botania:life_essence",
                    250,
                    "botania:livingrock",
                    new String[] {
                            "botania:livingrock",
                            "botania:livingwood",
                            "botania:dreamwood",

                            "botania:shimmerrock",
                            "botania:glimmering_livingwood",
                            "botania:glimmering_dreamwood",

                            "botania:dark_quartz",
                            "botania:mana_quartz",
                            "botania:blaze_quartz",
                            "botania:lavender_quartz",
                            "botania:red_quartz",
                            "botania:elf_quartz",
                            "botania:sunny_quartz",

                            "botania:cell_block",
                            "botania:enchanted_soil",

                            "botania:manasteel_block",
                            "botania:terrasteel_block",
                            "botania:elementium_block",
                            "botania:mana_diamond_block",
                            "botania:dragonstone_block",

                            "botania:mana_glass",
                            "botania:elf_glass",
                            "botania:bifrost_perm",

                            "botania:mana_pylon",
                            "botania:natura_pylon",
                            "botania:gaia_pylon",
                    },
                    new int[] { 200, 200, 100, 150, 150, 60, 80, 80, 80, 80, 80, 80, 80, 20, 20, 30, 30, 20, 30, 20, 60, 40, 20, 15, 10, 1 });

        }
    }
}
