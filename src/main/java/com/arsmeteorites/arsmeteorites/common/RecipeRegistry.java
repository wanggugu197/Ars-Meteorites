package com.arsmeteorites.arsmeteorites.common;

import com.arsmeteorites.arsmeteorites.ArsMeteorites;
import com.arsmeteorites.arsmeteorites.MeteoriteRitualConfig;
import com.arsmeteorites.arsmeteorites.common.recipe.builder.MeteoriteRegistryHelper;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import com.hollingsworth.arsnouveau.setup.registry.BlockRegistry;

import java.util.*;

import javax.annotation.Nullable;

import static com.arsmeteorites.arsmeteorites.common.recipe.BasicFormula.BasicFormulaRegistration;
import static com.arsmeteorites.arsmeteorites.common.recipe.LinkageFormula.LinkageFormulaRegistration;

@Mod.EventBusSubscriber(modid = ArsMeteorites.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class RecipeRegistry {

    public record MeteoriteType(String id, Item input, double source, int model, Item consumeitem, Block[] meteorites, int[] weights, int totalWeight, int[] layer) {}

    private static final Map<Item, MeteoriteType> TYPE_BY_INPUT = new HashMap<>();

    public static Collection<MeteoriteType> getAllTypes() {
        return Collections.unmodifiableCollection(TYPE_BY_INPUT.values());
    }

    @Nullable
    public static MeteoriteType getTypeByInput(Item input) {
        return TYPE_BY_INPUT.get(input);
    }

    public static boolean isItemCanConsume(Item targetItem) {
        return TYPE_BY_INPUT.containsKey(targetItem) ||
                TYPE_BY_INPUT.values().stream().anyMatch(type -> targetItem.equals(type.consumeitem())) ||
                targetItem == BlockRegistry.MENDOSTEEN_POD.get().asItem() ||
                targetItem == BlockRegistry.BASTION_POD.get().asItem() ||
                targetItem == BlockRegistry.FROSTAYA_POD.get().asItem() ||
                targetItem == BlockRegistry.BOMBEGRANTE_POD.get().asItem();
    }

    public static void registerMeteoriteType(MeteoriteType type) {
        MeteoriteType oldTypeById = TYPE_BY_INPUT.get(type.input());
        if (oldTypeById != null) {
            TYPE_BY_INPUT.remove(oldTypeById.input());
            TYPE_BY_INPUT.put(type.input(), type);
            // ArsMeteorites.LOGGER.warn("覆盖已存在的陨石类型: {}", type.id());
        } else {
            TYPE_BY_INPUT.put(type.input(), type);
            // ArsMeteorites.LOGGER.info("成功注册陨石类型: {} (输入物品: {})", type.id(), type.input());
        }
    }

    @SubscribeEvent
    public static void onAddReloadListeners(AddReloadListenerEvent event) {
        if (MeteoriteRitualConfig.ENABLE_RECIPE.get()) {
            BasicFormulaRegistration();
            LinkageFormulaRegistration();
        }
        if (MeteoriteRitualConfig.ENABLE_DTAT_RECIPE.get()) {
            event.addListener(new MeteoriteRegistryHelper());
        }
    }
}
