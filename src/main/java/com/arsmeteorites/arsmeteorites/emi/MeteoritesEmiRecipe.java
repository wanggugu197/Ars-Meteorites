package com.arsmeteorites.arsmeteorites.emi;

import com.arsmeteorites.arsmeteorites.ArsMeteorites;
import com.arsmeteorites.arsmeteorites.MeteoriteRitualConfig;
import com.arsmeteorites.arsmeteorites.common.RecipeRegistry;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.ForgeRegistries;

import com.hollingsworth.arsnouveau.setup.registry.BlockRegistry;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MeteoritesEmiRecipe implements EmiRecipe {

    private final RecipeRegistry.MeteoriteType recipe;
    private final ResourceLocation id;

    private final Item Catalysts = Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(
            new ResourceLocation(ArsMeteorites.MOD_ID, "ritual_conjure_meteorites")));

    public MeteoritesEmiRecipe(RecipeRegistry.MeteoriteType recipe) {
        this.recipe = recipe;
        this.id = new ResourceLocation(ArsMeteorites.MOD_ID, "/" + recipe.id().toLowerCase());
    }

    private final Item ConsumeItem = ForgeRegistries.ITEMS.getValue(
            new ResourceLocation(MeteoriteRitualConfig.RADIUS_INCREASE_ITEM.get()));

    @Override
    public EmiRecipeCategory getCategory() {
        return MeteoritesEmiPlugin.METEORITES_CATEGORY;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return recipe.input() != null ?
                List.of(EmiStack.of(recipe.input())) :
                List.of(EmiStack.of(Blocks.AIR));
    }

    @Override
    public List<EmiIngredient> getCatalysts() {
        return List.of(EmiStack.of(Catalysts));
    }

    @Override
    public List<EmiStack> getOutputs() {
        return Arrays.stream(recipe.meteorites())
                .map(EmiStack::of)
                .toList();
    }

    @Override
    public int getDisplayWidth() {
        return 150;
    }

    @Override
    public int getDisplayHeight() {
        return 145;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        int backgroundWidth = 150;
        int backgroundHeight = 145;

        Block[] meteorites = recipe.meteorites();
        int[] probabilities = recipe.weights();

        int[][] circles = {
                { 19, 8, 0 },
                { 34, 16, 11 },
                { 50, 24, 7 },
                { 66, 32, 5 }
        };

        int remainingItems = meteorites.length;
        int currentCircle = 0;

        int centerX = backgroundWidth / 2 - 8;
        int centerY = backgroundHeight / 2 - 8;

        while (remainingItems > 0 && currentCircle < circles.length) {
            int radius = circles[currentCircle][0];
            int capacity = circles[currentCircle][1];
            int angleOffset = circles[currentCircle][2];

            int itemsInThisCircle = Math.min(remainingItems, capacity);

            for (int i = 0; i < itemsInThisCircle; i++) {
                double angle = Math.toRadians(angleOffset + (360.0 * i / itemsInThisCircle));

                int x = centerX + (int) (radius * Math.cos(angle));
                int y = centerY + (int) (radius * Math.sin(angle));

                int itemIndex = meteorites.length - remainingItems + i;

                widgets.addSlot(EmiStack.of(meteorites[itemIndex]), x, y)
                        .appendTooltip(Component.translatable("tooltip.arsmeteorites.probability", probabilities[itemIndex])).recipeContext(this).drawBack(false);
            }

            remainingItems -= itemsInThisCircle;
            currentCircle++;
        }

        widgets.addSlot(EmiStack.of(BlockRegistry.RITUAL_BLOCK.asItem()), 2, backgroundHeight - 18).drawBack(false);

        widgets.addSlot(EmiStack.of(Catalysts), 2, backgroundHeight - 34).drawBack(false);

        if (recipe.input() != null) {
            widgets.addSlot(EmiStack.of(recipe.input()), 2, backgroundHeight - 50).drawBack(false);
        }

        if (ConsumeItem != null) {
            widgets.addSlot(EmiStack.of(ConsumeItem), 18, backgroundHeight - 34)
                    .appendTooltip(Component.translatable("tooltip.arsmeteorites.source_gem")).recipeContext(this).drawBack(false);
        }

        widgets.addText(
                Component.translatable("jei.arsmeteorites.source_cost", recipe.source()),
                20, backgroundHeight - 12, 0xFFFFFF, false);
    }
}
