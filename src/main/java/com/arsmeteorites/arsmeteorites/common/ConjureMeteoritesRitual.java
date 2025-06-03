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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

import com.hollingsworth.arsnouveau.api.ritual.AbstractRitual;
import com.hollingsworth.arsnouveau.api.util.SourceUtil;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ConjureMeteoritesRitual extends AbstractRitual {

    private int TargetRadius = 7;
    private int MaxRadius = 30;
    private int SourceCost = 5;
    private int defaultMeteoriteType = 0;
    private Item ConsumeItem = Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(
            new ResourceLocation("ars_nouveau:source_gem")));

    private int CurrentRadius = 0;
    private BlockPos center;
    private boolean isSafeToGenerate = false;

    private MeteoritesList currentMeteoriteType;

    private static final List<MeteoritesList> METEORITE_TYPES = RecipeRegistry.METEORITE_TYPES;
    private static final Map<String, Integer> WEIGHT_SUM_CACHE = RecipeRegistry.WEIGHT_SUM_CACHE;

    @Override
    public void onStart(@Nullable Player player) {
        super.onStart(player);
        Level world = getWorld();
        if (world != null && world.isClientSide) return;

        this.TargetRadius = MeteoriteRitualConfig.BASE_RADIUS.get();
        this.MaxRadius = MeteoriteRitualConfig.MAX_RADIUS.get();
        this.SourceCost = MeteoriteRitualConfig.SOURCE_COST_PER_BLOCK.get();
        this.defaultMeteoriteType = MeteoriteRitualConfig.DEFAULT_METEORITE_TYPE.get();
        this.ConsumeItem = Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(
                new ResourceLocation(MeteoriteRitualConfig.RADIUS_INCREASE_ITEM.get())));

        currentMeteoriteType = METEORITE_TYPES.get(defaultMeteoriteType);

        for (ItemStack stack : getConsumedItems()) {
            if (stack.is(ConsumeItem)) {
                TargetRadius = Math.min(TargetRadius + stack.getCount(), MaxRadius);
            } else {
                for (MeteoritesList type : METEORITE_TYPES) {
                    if (type.input() != null && stack.is(type.input())) {
                        currentMeteoriteType = type;
                        break;
                    }
                }
            }
        }
    }

    @Override
    protected void tick() {
        Level world = getWorld();

        if (world.getGameTime() % 2 == 0 && !world.isClientSide) {

            int high = TargetRadius * 2;

            center = Objects.requireNonNull(getPos()).above(high);

            if (!isSafeToGenerate) {
                if (hasNonAirBlocks(world, center, high)) {
                    SphereExplosion.explosion(center, world, TargetRadius * 3, false, false);
                    setFinished();
                } else {
                    isSafeToGenerate = true;
                    CurrentRadius = TargetRadius;
                }
            }

            if (CurrentRadius < 0) {
                world.playSound(null, getPos(), SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 4.0f, 0.5f);
                setFinished();
                return;
            }

            int blocksGenerated = generateMeteoriteLayer(world, CurrentRadius);

            CurrentRadius--;

            int sourceCost = Math.max(1, blocksGenerated * SourceCost);
            if (consumeSource(world, center, sourceCost)) {
                world.playSound(null, getPos(), SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0f, 1.0f);
                setFinished();
                return;
            }
        }

        world.playSound(null, Objects.requireNonNull(getPos()), SoundEvents.ANVIL_LAND, SoundSource.BLOCKS, 1.0f, 0.8f);
    }

    private int generateMeteoriteLayer(Level world, int radius) {
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
                    int zSq = z * z;
                    int distanceSq = xySq + zSq;

                    if (distanceSq <= radiusSq && (radius == TargetRadius || distanceSq > innerRadiusSq)) {
                        BlockPos pos = center.offset(x, y, z);
                        BlockState state = getRandomMeteoriteBlock();
                        world.setBlock(pos, state, 3);
                        blocksGenerated++;
                    }
                }
            }
        }
        return blocksGenerated;
    }

    private BlockState getRandomMeteoriteBlock() {
        Block[] blocks = currentMeteoriteType.Meteorites();
        int[] weights = currentMeteoriteType.Probability();

        Integer totalWeight = WEIGHT_SUM_CACHE.get(currentMeteoriteType.id());
        if (totalWeight == null || totalWeight == 0) {

            totalWeight = 0;
            for (int weight : weights) {
                totalWeight += weight;
            }
            WEIGHT_SUM_CACHE.put(currentMeteoriteType.id(), totalWeight);
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
        if (stack.is(ConsumeItem)) {
            return true;
        }
        for (MeteoritesList type : METEORITE_TYPES) {
            if (type.input() != null && stack.is(type.input())) {
                return true;
            }
        }
        return false;
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

    public record MeteoritesList(String id, Item input, Block[] Meteorites, int[] Probability) {}
}
