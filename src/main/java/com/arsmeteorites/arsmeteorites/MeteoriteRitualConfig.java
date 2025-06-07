package com.arsmeteorites.arsmeteorites;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ArsMeteorites.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)

public class MeteoriteRitualConfig {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static ForgeConfigSpec.IntValue BASE_RADIUS;
    public static ForgeConfigSpec.IntValue MAX_RADIUS;

    public static ForgeConfigSpec.BooleanValue ENABLE_RECIPE;
    public static ForgeConfigSpec.BooleanValue ENABLE_DTAT_RECIPE;

    static {
        initConfig();
        SPEC = BUILDER.build();
    }

    private static void initConfig() {
        BUILDER.push("Meteorite Ritual Settings");

        BASE_RADIUS = BUILDER.comment("Base radius of the meteorite sphere\n陨石球基础半径")
                .defineInRange("baseRadius", 7, 1, 100);

        MAX_RADIUS = BUILDER.comment("Maximum possible radius of a meteorite ball\n陨石球最大可能半径")
                .defineInRange("maxRadius", 30, 1, 100);

        ENABLE_RECIPE = BUILDER.comment("Enables recipes defined by this mod\n启用此模组定义的配方")
                .define("enableRecipe", true);

        ENABLE_DTAT_RECIPE = BUILDER.comment("""
                Enable data pack recipe loading
                启用数据包配方

                The module does not contain data pack recipes
                模组内不包含数据包配方
                If you need it, please register it yourself and put it in meteorite_recipes under the mod
                如果你需要请自行注册并放于mod下meteorite_recipes中
                The data packet needs to contain the following five elements
                数据包中需要包含一下五个元素

                input   source   catalysts   meteorites   weights
                string  double   string      string[]     int[]

                They are respectively used to determine which meteorite it is,
                他们分别是用于判断是那个陨石球的物品，
                the magic source consumption of each block,
                每个方块魔源消耗，
                Corresponding to items that expand the radius of meteorites,
                对应扩大陨石半径的物品，
                the array of blocks in the meteorite,
                陨石中方块的数组，
                and the array of weights corresponding to the meteorite blocks.
                对应陨石方块的权重的数组

                For example
                例如
                {
                  "input": "minecraft:obsidian",
                  "source": 120,
                  "catalysts": "ars_nouveau:source_gem",
                  "meteorites": [
                    "minecraft:obsidian",
                    "minecraft:basalt",
                    "minecraft:blackstone"
                  ],
                  "weights": [60, 30, 10]
                }""")
                .define("enableDataRecipe", true);

        BUILDER.pop();
    }
}
