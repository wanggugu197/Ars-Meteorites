package com.arsmeteorites.arsmeteorites;

import com.arsmeteorites.arsmeteorites.common.explode.EntityRegistry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(ArsMeteorites.MOD_ID)
public class ArsMeteorites {

    public static final String MOD_ID = "arsmeteorites";
    public static final String NAME = "Ars Meteorites";
    public static final Logger LOGGER = LogManager.getLogger(NAME);

    public ArsMeteorites() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, MeteoriteRitualConfig.SPEC, "arsmeteorites-ritual.toml");

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        EntityRegistry.init();
    }

    public static boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    public static Item getItem(String s) {
        return getItem(new ResourceLocation(s));
    }

    public static Item getItem(String mod, String name) {
        return getItem(new ResourceLocation(mod, name));
    }

    public static Item getItem(ResourceLocation id) {
        Item i = ForgeRegistries.ITEMS.getValue(id);
        if (i == Items.AIR) return Items.BARRIER;
        return i;
    }

    public static Block getBlock(String s) {
        return ForgeRegistries.BLOCKS.getValue(new ResourceLocation(s));
    }
}
