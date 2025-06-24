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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import com.hollingsworth.arsnouveau.api.ritual.AbstractRitual;
import com.hollingsworth.arsnouveau.api.util.SourceUtil;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import com.hollingsworth.arsnouveau.setup.registry.BlockRegistry;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ConjureMeteoritesRitual extends AbstractRitual {

    private int TargetRadius;
    private double SourceCost;
    private int model;
    private RecipeRegistry.MeteoriteType currentMeteoriteType;
    private final Object2IntMap<Item> map = new Object2IntOpenHashMap<>();

    private int CurrentRadius = 0;
    private int sourceCostNeeded = 0;
    private BlockPos center;
    private boolean isSafeToGenerate = false;
    private boolean isSourceCosted = true;

    private final int[] Number_of_blocks = { 1, 6, 26, 90, 134, 258, 410, 494, 690, 962,
            1098, 1406, 1578, 2018, 2342, 2634, 2930, 3402, 3926, 4266,
            4730, 5510, 5562, 6410, 6894, 7490, 8258, 8994, 9446, 9978,
            11138, 11406, 12578, 13490, 13962, 15062, 15690, 16826, 17454, 18890,
            19322, 20598, 21818, 22602, 23858, 25278, 25682, 26954, 28230, 29786,
            30738, 32186, 33326, 34626, 36314, 36750, 38810, 40458, 40802, 43502,
            44010, 46586, 47166, 49490, 50586, 51734, 54818, 54834, 57122, 59838,
            60122, 62370, 63710, 66290, 68682, 69626, 71598, 73658, 75122, 77334,
            78866, 82026, 82910, 86514, 87122, 89498, 92214, 94418, 95682, 97622,
            101970, 102986, 104426, 108414, 109130, 112290, 114230, 116762, 119874, 122834,
            123294, 126914, 130170, 131534, 134570, 138282, 138854, 142530, 145706, 146970,
            151334, 152762, 156450, 159206, 162210, 165002, 167322, 171398, 172898, 176130,
            178478, 183402, 185186, 189038, 191634, 195050, 199170, 198422, 204306, 208298,
            210542, 214146, 216986, 221898, 224390, 226362, 229658, 235286, 237090, 241346,
            243294, 250082, 250002, 255290, 258878, 262170, 267410, 269886, 272522, 277586 };

    @Override
    public void onStart(@Nullable Player player) {
        super.onStart(player);
        Level world = getWorld();
        if (world != null && world.isClientSide) return;

        int minRadius = MeteoriteRitualConfig.BASE_RADIUS.get();
        int maxRadius = MeteoriteRitualConfig.MAX_RADIUS.get();

        Item MeteoriteItem = Items.AIR;
        currentMeteoriteType = RecipeRegistry.getTypeByInput(Items.AIR);

        int XOffset = 0;
        int ZOffset = 0;

        for (ItemStack stack : getConsumedItems()) {
            Item consumable = stack.getItem();

            if (map.containsKey(consumable)) map.put(consumable, map.getInt(consumable) + 1);
            else map.put(consumable, 1);

            RecipeRegistry.MeteoriteType type = RecipeRegistry.getTypeByInput(consumable);

            if (type != null && MeteoriteItem == Items.AIR) {
                MeteoriteItem = consumable;
                SourceCost = type.source();
                model = type.model();
                currentMeteoriteType = type;
            }
        }

        TargetRadius = Math.min(minRadius + map.getInt(currentMeteoriteType.catalysts()), maxRadius);

        XOffset += map.getInt(BlockRegistry.FROSTAYA_POD.get().asItem());
        XOffset -= map.getInt(BlockRegistry.BOMBEGRANTE_POD.get().asItem());
        ZOffset += map.getInt(BlockRegistry.MENDOSTEEN_POD.get().asItem());
        ZOffset -= map.getInt(BlockRegistry.BASTION_POD.get().asItem());

        getCenter(world, XOffset, ZOffset, TargetRadius);
    }

    @Override
    protected void tick() {
        Level world = getWorld();

        if (world != null && world.getGameTime() % 2 == 0 && !world.isClientSide) {

            if (!isSafeToGenerate) {
                setFinished();
                return;
            }

            if (CurrentRadius >= TargetRadius) setFinished();

            if (isSourceCosted) {
                generateMeteoriteLayer(world, CurrentRadius);
                sourceCostNeeded = Math.max(1, (int) (Number_of_blocks[CurrentRadius + 1] * SourceCost));
                CurrentRadius++;
            }

            consumeSource(world, getPos());
        }

        if (world != null) {
            world.playSound(null, Objects.requireNonNull(getPos()), SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 2.0f, 1.0f);
            world.playSound(null, Objects.requireNonNull(getPos()), SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 8.0f, 0.5f);
        }
    }

    private void generateMeteoriteLayer(Level world, int radius) {
        if (radius == 0) {
            world.setBlock(center, getRandomMeteoriteBlock(), 3);
            return;
        }

        int radiusSq = radius * radius;
        int innerRadiusSq = (radius - 1) * (radius - 1);

        if (model == 0) {
            iterateSphereBlocks(radius, radiusSq, innerRadiusSq, (pos) -> {
                if (world.getBlockState(pos).isAir()) {
                    world.setBlock(pos, getRandomMeteoriteBlock(), 3);
                }
            });
        } else if (model == 1) {
            BlockState MeteoriteBlock = getFixedBlockForLayer();
            iterateSphereBlocks(radius, radiusSq, innerRadiusSq, (pos) -> {
                if (world.getBlockState(pos).isAir()) {
                    world.setBlock(pos, MeteoriteBlock, 3);
                }
            });
        }
    }

    private void iterateSphereBlocks(int radius, int radiusSq, int innerRadiusSq, java.util.function.Consumer<BlockPos> blockAction) {
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
                        BlockPos pos = center.offset(x, y, z);
                        blockAction.accept(pos);
                    }
                }
            }
        }
    }

    private BlockState getRandomMeteoriteBlock() {
        int totalWeight = currentMeteoriteType.totalWeight();
        Block[] blocks = currentMeteoriteType.meteorites();
        int[] weights = currentMeteoriteType.weights();

        int randomValue = rand.nextInt(totalWeight);
        int cumulativeWeight = 0;

        for (int i = 0; i < blocks.length; i++) {
            cumulativeWeight += weights[i];
            if (randomValue < cumulativeWeight) return blocks[i].defaultBlockState();
        }

        return blocks[0].defaultBlockState();
    }

    private BlockState getFixedBlockForLayer() {
        int totalWeight = currentMeteoriteType.totalWeight();
        Block[] blocks = currentMeteoriteType.meteorites();
        int[] weights = currentMeteoriteType.weights();

        int targetWeight = (totalWeight * CurrentRadius / TargetRadius);

        int cumulativeWeight = 0;
        for (int i = 0; i < blocks.length; i++) {
            cumulativeWeight += weights[i];
            if (targetWeight < cumulativeWeight) return blocks[i].defaultBlockState();
        }

        return blocks[blocks.length - 1].defaultBlockState();
    }

    private void getCenter(Level world, int XOffset, int ZOffset, int radius) {
        center = Objects.requireNonNull(getPos()).offset(XOffset, radius << 1, ZOffset);
        System.out.println("METEORITR_WAY 配置值: " + MeteoriteRitualConfig.METEORITR_WAY.get());

        if (MeteoriteRitualConfig.METEORITR_WAY.get()) {
            if (hasNonAirBlocks(world, center, radius << 1, radius)) {
                SphereExplosion.explosion(center, world, radius * 3);
                isSafeToGenerate = false;
            } else {
                isSafeToGenerate = true;
                CurrentRadius = 0;
            }
        } else {
            int destructionValue = (radius * radius * radius / 5);
            if (DestroyBlockJudgmentCenter(world, destructionValue, radius)) {
                isSafeToGenerate = false;
            } else {
                isSafeToGenerate = true;
                CurrentRadius = 0;
            }
        }
    }

    private boolean hasNonAirBlocks(Level world, BlockPos center, int high, int radius) {
        int checkRadius = (int) (radius * 1.2f);
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

    private boolean DestroyBlockJudgmentCenter(Level world, int destructionValue, int radius) {
        final int maxY = world.getMaxBuildHeight();
        final int minY = world.getMinBuildHeight();
        int destruction = destructionValue;

        for (int y = maxY; y >= minY; y--) {
            center = center.atY(y);
            float hardnessSum = 0.0f;

            for (int dx = -2; dx <= 2; dx++) {
                for (int dz = -2; dz <= 2; dz++) {
                    BlockPos pos = center.offset(dx, 0, dz);
                    BlockState state = world.getBlockState(pos);

                    if (state.getBlock() == Blocks.BEDROCK) destruction = -1;

                    if (!state.isAir()) {
                        float hardness = state.getDestroySpeed(world, pos);
                        if (hardness >= 0) hardnessSum += hardness;
                    }
                }
            }

            destruction -= (int) hardnessSum;

            if (destruction > 0) {
                int innerRad = (int) (radius * 0.5f);
                int outerRad = (int) (radius * 0.7f);
                int innerRadSq = (innerRad * innerRad);
                int outerRadSq = (outerRad * outerRad);

                for (int dx = -(int) outerRad; dx <= outerRad; dx++) {
                    for (int dz = -(int) outerRad; dz <= outerRad; dz++) {
                        int distSq = dx * dx + dz * dz;
                        BlockPos pos = center.offset(dx, 0, dz);

                        if (distSq <= innerRadSq) world.destroyBlock(pos, false);
                        else if (distSq <= outerRadSq)
                            if (world.random.nextInt(100) < 70) world.destroyBlock(pos, false);
                    }
                }
            }

            if (destruction <= 0) {
                center = center.atY(y + (int) (radius * 0.8f));
                return false;
            }
        }
        return true;
    }

    private void consumeSource(Level world, BlockPos center) {
        setNeedsSource(true);
        while (sourceCostNeeded > 0) {
            int amountToTake = Math.min(sourceCostNeeded, 5000);
            if (SourceUtil.takeSource(center, world, 6, amountToTake) == null) {
                isSourceCosted = false;
                return;
            }
            sourceCostNeeded -= amountToTake;
        }
        setNeedsSource(false);
        isSourceCosted = true;
    }

    @Override
    public boolean canConsumeItem(ItemStack stack) {
        Item item = stack.getItem();
        return RecipeRegistry.isItemCanConsume(item);
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
