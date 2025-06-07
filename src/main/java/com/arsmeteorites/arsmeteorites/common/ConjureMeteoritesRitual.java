package com.arsmeteorites.arsmeteorites.common;

import com.arsmeteorites.arsmeteorites.ArsMeteorites;
import com.arsmeteorites.arsmeteorites.MeteoriteRitualConfig;
import com.arsmeteorites.arsmeteorites.common.explode.SphereExplosion;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import com.hollingsworth.arsnouveau.api.ritual.AbstractRitual;
import com.hollingsworth.arsnouveau.api.util.SourceUtil;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import com.hollingsworth.arsnouveau.setup.registry.ItemsRegistry;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ConjureMeteoritesRitual extends AbstractRitual {

    private int TargetRadius;
    private double SourceCost;
    private RecipeRegistry.MeteoriteType currentMeteoriteType;
    private final Object2IntMap<Item> map = new Object2IntOpenHashMap<>();

    private int CurrentRadius = 0;
    private BlockPos center;
    private boolean isSafeToGenerate = false;

    @Override
    public void onStart(@Nullable Player player) {
        super.onStart(player);
        Level world = getWorld();
        if (world != null && world.isClientSide) return;

        int minRadius = MeteoriteRitualConfig.BASE_RADIUS.get();
        int maxRadius = MeteoriteRitualConfig.MAX_RADIUS.get();

        Item consumeItem = ItemsRegistry.SOURCE_GEM.get().asItem();
        Item MeteoriteItem = Items.AIR;
        currentMeteoriteType = RecipeRegistry.getTypeByInput(Items.AIR);

        for (ItemStack stack : getConsumedItems()) {
            Item consumable = stack.getItem();

            if (map.containsKey(consumable)) {
                map.put(consumable, map.getInt(consumable) + 1);
            } else {
                map.put(consumable, 1);
            }

            RecipeRegistry.MeteoriteType type = RecipeRegistry.getTypeByInput(consumable);

            if (type != null && MeteoriteItem == Items.AIR) {
                MeteoriteItem = consumable;
                SourceCost = type.source();
                consumeItem = type.catalysts();
                currentMeteoriteType = type;
            }

            if (stack.is(consumeItem)) {
                TargetRadius = Math.min(minRadius + map.getInt(consumable), maxRadius);
            }
        }
    }

    @Override
    protected void tick() {
        Level world = getWorld();

        if (world != null && world.getGameTime() % 2 == 0 && !world.isClientSide) {

            int high = TargetRadius << 1;

            center = Objects.requireNonNull(getPos()).above(high);

            if (!isSafeToGenerate) {
                if (hasNonAirBlocks(world, center, high)) {
                    SphereExplosion.explosion(center, world, TargetRadius * 3, false, false);
                    setFinished();
                } else {
                    isSafeToGenerate = true;
                    CurrentRadius = 0;
                }
            }

            if (CurrentRadius >= TargetRadius) {
                world.playSound(null, Objects.requireNonNull(getPos()), SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 2.0f, 0.5f);
                world.playSound(null, Objects.requireNonNull(getPos()), SoundEvents.ANVIL_LAND, SoundSource.BLOCKS, 4.0f, 0.8f);
                setFinished();
                return;
            }

            int blocksGenerated = generateMeteoriteLayer(world, CurrentRadius);

            CurrentRadius++;

            int sourceCost = Math.max(1, (int) (blocksGenerated * SourceCost));
            if (consumeSource(world, center, sourceCost)) {
                world.playSound(null, Objects.requireNonNull(getPos()), SoundEvents.ANVIL_LAND, SoundSource.BLOCKS, 4.0f, 0.8f);
                setFinished();
                return;
            }
        }

        if (world != null) {
            world.playSound(null, Objects.requireNonNull(getPos()), SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 2.0f, 0.5f);
            world.playSound(null, Objects.requireNonNull(getPos()), SoundEvents.ANVIL_LAND, SoundSource.BLOCKS, 4.0f, 0.8f);
        }
    }

    private int generateMeteoriteLayer(Level world, int radius) {
        if (radius == 0) {
            world.setBlock(center, getRandomMeteoriteBlock(), 3);
            return 1;
        }

        int blocksGenerated = 0;
        int radiusSq = radius * radius;
        int innerRadiusSq = (radius - 1) * (radius - 1);

        for (int x = -radius; x <= radius; x++) {
            int xSq = x * x;

            if (xSq > radiusSq) continue;

            for (int y = -radius; y <= radius; y++) {
                int ySq = y * y;
                int xySq = xSq + ySq;

                if (xySq > radiusSq) continue;

                for (int z = -radius; z <= radius; z++) {
                    int distanceSq = xySq + z * z;
                    if (distanceSq <= radiusSq && distanceSq > innerRadiusSq) {
                        world.setBlock(center.offset(x, y, z), getRandomMeteoriteBlock(), 3);
                        blocksGenerated++;
                    }
                }
            }
        }
        return blocksGenerated;
    }

    private BlockState getRandomMeteoriteBlock() {
        int totalWeight = currentMeteoriteType.totalWeight();
        Block[] blocks = currentMeteoriteType.meteorites();
        int[] weights = currentMeteoriteType.weights();

        if (totalWeight <= 0 || blocks.length == 0) {
            return blocks[0].defaultBlockState();
        }

        int randomValue = rand.nextInt(totalWeight);
        int cumulativeWeight = 0;

        for (int i = 0; i < blocks.length; i++) {
            cumulativeWeight += weights[i];
            if (randomValue < cumulativeWeight) {
                return blocks[i].defaultBlockState();
            }
        }

        return blocks[0].defaultBlockState();
    }

    private boolean hasNonAirBlocks(Level world, BlockPos center, int high) {
        int checkRadius = (int) (TargetRadius * 1.2f);
        int checkRadiusSq = checkRadius * checkRadius;

        for (int y = -high + 1; y <= 0; y++) {
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    if (!world.isEmptyBlock(center.offset(x, y, z))) {
                        return true;
                    }
                }
            }
        }

        for (int y = -high + 1; y <= 0; y += 2) {
            int ySq = y * y;
            if (ySq > checkRadiusSq) continue;
            for (int x = -checkRadius; x <= checkRadius; x += 5) {
                int xSq = x * x;
                if (xSq > checkRadiusSq) continue;
                for (int z = -checkRadius; z <= checkRadius; z += 3) {
                    if (xSq + ySq + z * z <= checkRadiusSq) {
                        if (!world.isEmptyBlock(center.offset(x, y, z))) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean consumeSource(Level world, BlockPos center, int amount) {
        setNeedsSource(true);
        int remaining = amount;
        while (remaining > 0) {
            int amountToTake = Math.min(remaining, 5000);
            if (SourceUtil.takeSource(center, world, 6, amountToTake) == null) {
                return false;
            }
            remaining -= amountToTake;
        }
        setNeedsSource(false);
        return true;
    }

    @Override
    public boolean canConsumeItem(ItemStack stack) {
        Item item = stack.getItem();
        return RecipeRegistry.isItemCanConsume(item);
    }

    @Override
    public int getSourceCost() {
        return 1;
    }

    @Override
    public ResourceLocation getRegistryName() {
        return new ResourceLocation(ArsMeteorites.MOD_ID, "ritual_conjure_meteorites");
    }

    @Override
    public ParticleColor getCenterColor() {
        return new ParticleColor(255, 100, 0);
    }

    @Override
    public String getLangName() {
        return "arsmeteorites.ritual_conjure_meteorites";
    }

    @Override
    public String getLangDescription() {
        return "arsmeteorites.ritual_desc.ritual_conjure_meteorites";
    }

    @Override
    public int getParticleIntensity() {
        return 250;
    }
}
