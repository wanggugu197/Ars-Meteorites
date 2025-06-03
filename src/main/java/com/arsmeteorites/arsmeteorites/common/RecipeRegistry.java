package com.arsmeteorites.arsmeteorites.common;

import com.arsmeteorites.arsmeteorites.MeteoriteRitualConfig;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.arsmeteorites.arsmeteorites.common.recipe.BasicFormula.BasicFormulaRegistration;
import static com.arsmeteorites.arsmeteorites.common.recipe.LinkageFormula.LinkageFormulaRegistration;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class RecipeRegistry {

    public static final List<ConjureMeteoritesRitual.MeteoritesList> METEORITE_TYPES = new ArrayList<>();
    public static final Map<String, Integer> WEIGHT_SUM_CACHE = new HashMap<>();

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        if (MeteoriteRitualConfig.ENABLE_RECIPE.get()) {
            BasicFormulaRegistration();

            LinkageFormulaRegistration();
        }
    }

    public static void registerMeteoriteType(ConjureMeteoritesRitual.MeteoritesList type) {
        METEORITE_TYPES.add(type);

        int sum = 0;
        for (int weight : type.Probability()) {
            sum += weight;
        }
        WEIGHT_SUM_CACHE.put(type.id(), sum);
    }
}
