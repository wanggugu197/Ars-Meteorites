// RecipeRegistry.java
package com.arsmeteorites.arsmeteorites.common;

import com.arsmeteorites.arsmeteorites.MeteoriteRitualConfig;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.*;

import javax.annotation.Nullable;

import static com.arsmeteorites.arsmeteorites.common.recipe.BasicFormula.BasicFormulaRegistration;
import static com.arsmeteorites.arsmeteorites.common.recipe.LinkageFormula.LinkageFormulaRegistration;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class RecipeRegistry {

    public record MeteoriteType(
                                String id,
                                Item input,
                                int source,
                                Block[] meteorites,
                                int[] weights,
                                int totalWeight) {

        public MeteoriteType {
            if (meteorites.length != weights.length) {
                throw new IllegalArgumentException("Meteorites and weights array lengths must match");
            }
        }
    }

    private static final Map<String, MeteoriteType> TYPE_BY_ID = new HashMap<>();
    private static final Map<Item, MeteoriteType> TYPE_BY_INPUT = new HashMap<>();

    public static Collection<MeteoriteType> getAllTypes() {
        return Collections.unmodifiableCollection(TYPE_BY_ID.values());
    }

    @Nullable
    public static MeteoriteType getTypeByInput(Item input) {
        return TYPE_BY_INPUT.get(input);
    }

    @Nullable
    public static MeteoriteType getTypeById(String id) {
        return TYPE_BY_ID.get(id);
    }

    public static int getTotalWeight(String id) {
        MeteoriteType type = TYPE_BY_ID.get(id);
        return type != null ? type.totalWeight() : 0;
    }

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        if (MeteoriteRitualConfig.ENABLE_RECIPE.get()) {
            BasicFormulaRegistration();
            LinkageFormulaRegistration();
        }
    }

    public static void registerMeteoriteType(MeteoriteType type) {
        if (TYPE_BY_ID.containsKey(type.id())) {
            throw new IllegalStateException("Duplicate meteorite type ID: " + type.id());
        }
        if (TYPE_BY_INPUT.containsKey(type.input())) {
            throw new IllegalStateException("Duplicate input item for meteorite type: " + type.input());
        }

        TYPE_BY_ID.put(type.id(), type);
        TYPE_BY_INPUT.put(type.input(), type);
    }
}
