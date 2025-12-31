package com.arsmeteorites.arsmeteorites;

import com.arsmeteorites.arsmeteorites.common.ConjureMeteoritesRitual;
import com.arsmeteorites.arsmeteorites.common.explode.EntityRegistry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

import com.hollingsworth.arsnouveau.setup.registry.APIRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(ArsMeteorites.MOD_ID)
public class ArsMeteorites {

    public static final String MOD_ID = "arsmeteorites";
    public static final String NAME = "Ars Meteorites";
    public static final Logger LOGGER = LogManager.getLogger(NAME);

    public ArsMeteorites(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, MeteoriteRitualConfig.SPEC, "arsmeteorites-ritual.toml");

        APIRegistry.registerRitual(new ConjureMeteoritesRitual());

        EntityRegistry.init();
    }

    public static boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    public static Item getItem(String s) {
        return getItem(ResourceLocation.parse(s));
    }

    public static Item getItem(String mod, String name) {
        return getItem(ResourceLocation.fromNamespaceAndPath(mod, name));
    }

    public static Item getItem(ResourceLocation id) {
        Item i = BuiltInRegistries.ITEM.get(id);
        if (i == Items.AIR) return Items.BARRIER;
        return i;
    }

    public static Block getBlock(String s) {
        ResourceLocation id = ResourceLocation.parse(s);
        return BuiltInRegistries.BLOCK.get(id);
    }
}
