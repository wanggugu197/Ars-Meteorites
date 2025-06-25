package com.arsmeteorites.arsmeteorites;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ArsMeteorites.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)

public class MeteoriteRitualConfig {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static ForgeConfigSpec.IntValue BASE_RADIUS;
    public static ForgeConfigSpec.IntValue MAX_RADIUS;
    public static ForgeConfigSpec.BooleanValue METEORITR_WAY;

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

        METEORITR_WAY = BUILDER.comment("true--Call of the Dark Rift--陨石召唤\nfalse--Mark of the Falling Tower--坠星标位")
                .define("MeteoriteWay", true);

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

                An optional element is also required int model default value 0
                还需要一个可选元素 int model 默认值 0
                The following is the corresponding relationship
                下面是对应关系
                0 Corresponding to Completely random meteorites
                0 对应 完全的随机陨石
                1 Corresponding to Fixed layer meteorite
                1 对应 固定的层陨石
                2 Corresponding to Random layer Meteorites
                2 对应 随机的层陨石
                3 Corresponding to Layered random meteorites
                3 对应 分层的随机陨石
                4 Corresponding to Random layer meteorites with layer restrictions
                4 对应 分层的随机层陨石

                When model is 3 or 4, the parameter int[] layer is required to set additional layers
                当model为3或4时需要参数 int[] layer 来进行额外的层设置
                int[] layer contains an even number of parameters, which are the number of blocks in meteorites corresponding to each layer and the weight of each layer
                int[] layer 中包含偶数个参数，分别为每层对应 meteorites 中方块的数量，和每层的权重
                For example, "layer": [1, 5, 2, 10, 30, 60] means that this meteorite has 3 layers with a total weight of 100
                If the radius of the meteorite is 30, the first layer is the first block in meteorites, occupying a radius of 0-3,
                the second layer is the second to sixth blocks in meteorites, occupying a radius of 4-12,
                and the third layer is the seventh to eighth blocks in meteorites, occupying a radius of 13-30.
                比如 "layer": [1, 5, 2, 10, 30, 60] 代表这个陨石有3层，总权重为100
                假如陨石半径为30，第1层是meteorites中第1个方块，占据半径0-3，第2层是meteorites中第2-6个方块，占据半径4-12，第3层是meteorites中第7-8个方块，占据半径13-30

                For example
                例如
                {
                  "input": "minecraft:obsidian",
                  "source": 120,
                  "default": 0,     //Optional 可选
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
