package com.arsmeteorites.arsmeteorites.jei;

import com.arsmeteorites.arsmeteorites.ArsMeteorites;
import com.arsmeteorites.arsmeteorites.common.RecipeRegistry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

import com.hollingsworth.arsnouveau.setup.registry.BlockRegistry;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class MeteoritesRecipeCategory implements IRecipeCategory<RecipeRegistry.MeteoriteType> {

    private final IDrawable background;
    private final IDrawable icon;
    private final Component title;
    private final Font font;

    public MeteoritesRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(150, 145);
        this.icon = guiHelper.createDrawableItemStack(
                new ItemStack(BlockRegistry.RITUAL_BLOCK.asItem()));
        this.title = Component.translatable("jei.category.arsmeteorites.meteorites");
        this.font = Minecraft.getInstance().font;
    }

    @Override
    public @NotNull RecipeType<RecipeRegistry.MeteoriteType> getRecipeType() {
        return MeteoritesJeiPlugin.METEORITES_RECIPE_TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return title;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void draw(RecipeRegistry.MeteoriteType recipe, @NotNull IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        Component costText = Component.translatable("jei.arsmeteorites.source_cost", recipe.source());
        guiGraphics.drawString(font, costText, 20, background.getHeight() - 12, 0xFFFFFF, false);
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, RecipeRegistry.MeteoriteType recipe, @NotNull IFocusGroup focuses) {
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

                builder.addSlot(RecipeIngredientRole.OUTPUT, x, y)
                        .addItemStack(new ItemStack(meteorites[itemIndex])).addRichTooltipCallback((recipeSlotView, tooltip) -> tooltip.add(Component.translatable("tooltip.arsmeteorites.probability", probabilities[itemIndex])));
            }

            remainingItems -= itemsInThisCircle;
            currentCircle++;
        }

        builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 2, backgroundHeight - 18)
                .addItemStack(new ItemStack(BlockRegistry.RITUAL_BLOCK.asItem()));

        builder.addSlot(RecipeIngredientRole.CATALYST, 2, backgroundHeight - 34)
                .addItemStack(new ItemStack(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(
                        new ResourceLocation(ArsMeteorites.MOD_ID, "ritual_conjure_meteorites")))));

        if (recipe.input() != null) {
            builder.addSlot(RecipeIngredientRole.INPUT, 2, backgroundHeight - 50)
                    .addItemStack(new ItemStack(recipe.input())).addRichTooltipCallback((recipeSlotView, tooltip) -> tooltip.add(Component.translatable("tooltip.arsmeteorites.input")));
        }

        builder.addSlot(RecipeIngredientRole.CATALYST, 18, backgroundHeight - 34)
                .addItemStack(new ItemStack(recipe.catalysts())).addRichTooltipCallback((recipeSlotView, tooltip) -> tooltip.add(Component.translatable("tooltip.arsmeteorites.source_gem")));
    }
}
