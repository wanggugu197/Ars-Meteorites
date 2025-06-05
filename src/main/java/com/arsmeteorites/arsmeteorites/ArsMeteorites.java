package com.arsmeteorites.arsmeteorites;

import com.arsmeteorites.arsmeteorites.common.explode.EntityRegistry;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(ArsMeteorites.MOD_ID)
public class ArsMeteorites {

    public static final String MOD_ID = "arsmeteorites";
    public static final String NAME = "Ars Meteorites";
    public static final Logger LOGGER = LogManager.getLogger(NAME);

    public ArsMeteorites() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        EntityRegistry.init();

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, MeteoriteRitualConfig.SPEC, "arsmeteorites-ritual.toml");
    }

    public static boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }
}
