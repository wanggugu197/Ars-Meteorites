package com.arsmeteorites.arsmeteorites.emi;

import com.arsmeteorites.arsmeteorites.ArsMeteorites;
import com.arsmeteorites.arsmeteorites.common.RecipeRegistry;

import net.minecraft.resources.ResourceLocation;

import com.hollingsworth.arsnouveau.setup.registry.BlockRegistry;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;

@dev.emi.emi.api.EmiEntrypoint
public class MeteoritesEmiPlugin implements EmiPlugin {

    public static final EmiRecipeCategory METEORITES_CATEGORY = new EmiRecipeCategory(
            ResourceLocation.fromNamespaceAndPath(ArsMeteorites.MOD_ID, "meteorites"),
            EmiStack.of(BlockRegistry.RITUAL_BLOCK.asItem()));

    @Override
    public void register(EmiRegistry registry) {
        registry.addCategory(METEORITES_CATEGORY);
        RecipeRegistry.getAllTypes().forEach(type -> registry.addRecipe(new MeteoritesEmiRecipe(type)));
    }
}
