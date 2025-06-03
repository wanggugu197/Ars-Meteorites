package com.arsmeteorites.arsmeteorites;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ArsMeteorites.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)

public class MeteoriteRitualConfig {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static ForgeConfigSpec.IntValue BASE_RADIUS;
    public static ForgeConfigSpec.IntValue MAX_RADIUS;
    public static ForgeConfigSpec.IntValue SOURCE_COST_PER_BLOCK;
    public static ForgeConfigSpec.IntValue DEFAULT_METEORITE_TYPE;
    public static ForgeConfigSpec.BooleanValue ENABLE_RECIPE;
    public static ForgeConfigSpec.BooleanValue ENABLE_CUSTOM_TYPES;
    public static ForgeConfigSpec.ConfigValue<String> RADIUS_INCREASE_ITEM;

    static {
        initConfig();
        SPEC = BUILDER.build();
    }

    private static void initConfig() {
        BUILDER.push("Meteorite Ritual Settings");

        BASE_RADIUS = BUILDER.comment("Base radius of the meteorite sphere")
                .defineInRange("baseRadius", 7, 5, 100);

        MAX_RADIUS = BUILDER.comment("Maximum possible radius")
                .defineInRange("maxRadius", 30, 5, 100);

        SOURCE_COST_PER_BLOCK = BUILDER.comment("Source cost per generated block")
                .defineInRange("sourceCostPerBlock", 5, 1, 100);

        DEFAULT_METEORITE_TYPE = BUILDER.comment("Default meteorite type index (from the registry)")
                .defineInRange("defaultMeteoriteType", 0, 0, 2000000000);

        ENABLE_RECIPE = BUILDER.comment("Enables recipes defined by this mod")
                .define("enableRecipe", true);

        ENABLE_CUSTOM_TYPES = BUILDER.comment("Enable custom meteorite types from config")
                .define("enableCustomTypes", true);

        RADIUS_INCREASE_ITEM = BUILDER.comment("Item ID used to increase the meteorite radius (default: ars_nouveau:source_gem)")
                .define("radiusIncreaseItem", "ars_nouveau:source_gem");

        BUILDER.pop();
    }
}
