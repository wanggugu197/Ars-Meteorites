package com.arsmeteorites.arsmeteorites.jei;

import com.arsmeteorites.arsmeteorites.ArsMeteorites;
import com.arsmeteorites.arsmeteorites.common.RecipeRegistry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

@JeiPlugin
public class MeteoritesJeiPlugin implements IModPlugin {

    public static final RecipeType<RecipeRegistry.MeteoriteType> METEORITES_RECIPE_TYPE = RecipeType.create(ArsMeteorites.MOD_ID, "meteorites", RecipeRegistry.MeteoriteType.class);

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return new ResourceLocation(ArsMeteorites.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new MeteoritesRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        // 使用新的 getAllTypes() 方法
        registration.addRecipes(METEORITES_RECIPE_TYPE, List.copyOf(RecipeRegistry.getAllTypes()));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(
                new ItemStack(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(
                        new ResourceLocation(ArsMeteorites.MOD_ID, "ritual_conjure_meteorites")))),
                METEORITES_RECIPE_TYPE);
    }
}
