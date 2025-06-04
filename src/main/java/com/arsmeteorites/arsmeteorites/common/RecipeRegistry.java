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
                                int totalWeight) {}

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

    public static void registerMeteoriteType(MeteoriteType type) {
        MeteoriteType oldTypeById = TYPE_BY_ID.get(type.id());
        if (oldTypeById != null) {
            TYPE_BY_INPUT.remove(oldTypeById.input());
            TYPE_BY_INPUT.put(type.input(), type);
            // LOGGER.warn("覆盖已存在的陨石类型: {}", type.id());
        } else {
            TYPE_BY_ID.put(type.id(), type);
            TYPE_BY_INPUT.put(type.input(), type);
            // LOGGER.info("成功注册陨石类型: {} (输入物品: {})", type.id(), type.input());
        }
    }

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        if (MeteoriteRitualConfig.ENABLE_RECIPE.get()) {
            BasicFormulaRegistration();
            LinkageFormulaRegistration();
        }
    }
}
