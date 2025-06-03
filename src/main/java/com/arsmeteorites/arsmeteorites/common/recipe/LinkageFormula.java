package com.arsmeteorites.arsmeteorites.common.recipe;

import com.arsmeteorites.arsmeteorites.common.recipe.builder.MeteoriteRegistryHelper;

public class LinkageFormula {

    public static void LinkageFormulaRegistration() {
        MeteoriteRegistryHelper.registerMeteoriteType(
                "ars_nouveau:archwood_planks",
                50,
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
    }
}
