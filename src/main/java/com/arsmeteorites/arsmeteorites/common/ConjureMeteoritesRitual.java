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

    private int TargetRadius = 7;
    private double SourceCost = 1;
    private int model = 0;
    private RecipeRegistry.MeteoriteType currentMeteoriteType;

    private int CurrentRadius = 0;
    private int sourceCostNeeded = 0;
    private BlockPos center = new BlockPos(0, 0, 0);
    private int checkTime = 0;
    private boolean isCenterCheck = false;
    private int destructionValue = 0;

    private final int[] Number_of_blocks = { 1, 7, 26, 90, 134, 258, 410, 494, 690, 962,
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

        Object2IntMap<Item> map = new Object2IntOpenHashMap<>();

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

        TargetRadius = Math.min(minRadius + map.getInt(currentMeteoriteType.consumeitem()), maxRadius);

        ZOffset += map.getInt(BlockRegistry.FROSTAYA_POD.get().asItem());
        ZOffset -= map.getInt(BlockRegistry.BOMBEGRANTE_POD.get().asItem());
        XOffset += map.getInt(BlockRegistry.MENDOSTEEN_POD.get().asItem());
        XOffset -= map.getInt(BlockRegistry.BASTION_POD.get().asItem());

        checkTime = 500;
        isCenterCheck = false;
        center = Objects.requireNonNull(getPos()).offset(XOffset, TargetRadius << 1, ZOffset);
    }

    @Override
    protected void tick() {
        Level world = getWorld();

        if (world != null && world.getGameTime() % 2 == 0 && !world.isClientSide) {
            if (!isCenterCheck) {
                if (checkTime <= 0) setFinished();
                else getCenter(world, TargetRadius);
                return;
            }

            if (CurrentRadius >= TargetRadius) setFinished();

            if (sourceCostNeeded == 0) {
                generateMeteoriteLayer(world, center, CurrentRadius, (double) CurrentRadius / TargetRadius);
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

    private void generateMeteoriteLayer(Level world, BlockPos center, int CurrentRadius, double proportion) {
        if (CurrentRadius == 0) {
            world.setBlock(center, currentMeteoriteType.meteorites()[0].defaultBlockState(), 3);
            return;
        }

        int radiusSq = CurrentRadius * CurrentRadius;
        int innerRadiusSq = (CurrentRadius - 1) * (CurrentRadius - 1);

        if (model == 0) {
            iterateSphereBlocks(world, center, CurrentRadius, radiusSq, innerRadiusSq, (pos) -> world.setBlock(pos, getRandomMeteoriteBlock(), 3));
        } else if (model == 1) {
            BlockState MeteoriteBlock = getFixedBlockForLayer(proportion);
            iterateSphereBlocks(world, center, CurrentRadius, radiusSq, innerRadiusSq, (pos) -> world.setBlock(pos, MeteoriteBlock, 3));
        } else if (model == 2) {
            BlockState MeteoriteBlock = getRandomMeteoriteBlock();
            iterateSphereBlocks(world, center, CurrentRadius, radiusSq, innerRadiusSq, (pos) -> world.setBlock(pos, MeteoriteBlock, 3));
        } else if (model == 3) {
            int Layer = getLayeredMeteoriteBlockA(proportion);
            iterateSphereBlocks(world, center, CurrentRadius, radiusSq, innerRadiusSq, (pos) -> world.setBlock(pos, getLayeredMeteoriteBlockB(Layer), 3));
        } else if (model == 4) {
            BlockState MeteoriteBlock = getLayeredMeteoriteBlockB(getLayeredMeteoriteBlockA(proportion));
            iterateSphereBlocks(world, center, CurrentRadius, radiusSq, innerRadiusSq, (pos) -> world.setBlock(pos, MeteoriteBlock, 3));
        }
    }

    private void iterateSphereBlocks(Level world, BlockPos center, int CurrentRadius, int radiusSq, int innerRadiusSq, java.util.function.Consumer<BlockPos> blockAction) {
        for (int x = -CurrentRadius; x <= CurrentRadius; x++) {
            int xSq = x * x;

            if (xSq > radiusSq) continue;

            for (int y = -CurrentRadius; y <= CurrentRadius; y++) {
                int ySq = y * y;
                int xySq = xSq + ySq;

                if (xySq > radiusSq) continue;

                for (int z = -CurrentRadius; z <= CurrentRadius; z++) {
                    int distanceSq = xySq + z * z;
                    if (distanceSq <= radiusSq && distanceSq > innerRadiusSq) {
                        BlockPos pos = center.offset(x, y, z);
                        if (world.getBlockState(pos).isAir()) {
                            blockAction.accept(pos);
                        }
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

    private BlockState getFixedBlockForLayer(double proportion) {
        int totalWeight = currentMeteoriteType.totalWeight();
        Block[] blocks = currentMeteoriteType.meteorites();
        int[] weights = currentMeteoriteType.weights();

        int targetWeight = (int) (totalWeight * proportion);

        int cumulativeWeight = 0;
        for (int i = 0; i < blocks.length; i++) {
            cumulativeWeight += weights[i];
            if (targetWeight < cumulativeWeight) return blocks[i].defaultBlockState();
        }

        return blocks[blocks.length - 1].defaultBlockState();
    }

    private int getLayeredMeteoriteBlockA(double proportion) {
        int[] layerData = currentMeteoriteType.layer();

        int totalLayers = layerData[layerData.length - 2];
        int layers = 0;
        int layersTargetWeight = (int) (layerData[layerData.length - 1] * proportion);

        int layersCumulativeWeight = 0;
        for (int i = 0; i < totalLayers; i++) {
            layersCumulativeWeight += layerData[totalLayers + i];
            if (layersTargetWeight <= layersCumulativeWeight) {
                layers = i;
                break;
            }
        }
        return layers;
    }

    private BlockState getLayeredMeteoriteBlockB(int layers) {
        int[] layerData = currentMeteoriteType.layer();
        Block[] blocks = currentMeteoriteType.meteorites();
        int[] weights = currentMeteoriteType.weights();

        int targetWeight = rand.nextInt(layerData[layerData[layerData.length - 2] * 2 + layers]);

        int cumulativeWeight = 0;
        int startingBlock = 0;
        for (int i = 0; i < layers; i++) startingBlock += layerData[i];
        for (int i = 0; i < layerData[layers]; i++) {
            cumulativeWeight += weights[startingBlock + i];
            if (targetWeight < cumulativeWeight) return blocks[startingBlock + i].defaultBlockState();
        }
        return blocks[startingBlock].defaultBlockState();
    }

    private void getCenter(Level world, int TargetRadius) {
        if (MeteoriteRitualConfig.METEORITR_WAY.get()) {
            if (hasNonAirBlocks(world, center, TargetRadius << 1, TargetRadius)) {
                SphereExplosion.explosion(center, world, TargetRadius * 3);
                isCenterCheck = false;
            } else {
                isCenterCheck = true;
                CurrentRadius = 0;
            }
        } else {
            for (int i = 0; i < 4; i++) {
                if (checkTime == 500) {
                    center = center.atY(world.getMaxBuildHeight());
                    destructionValue = (TargetRadius * TargetRadius * TargetRadius / 5);
                    checkTime--;
                }
                destructionValue -= DestroyBlockJudgmentCenter(world, center, TargetRadius);
                if (destructionValue > 0) {
                    center = center.below(1);
                    checkTime--;
                } else {
                    isCenterCheck = true;
                    CurrentRadius = 0;
                    center = center.atY(center.getY() + (int) (TargetRadius * 0.8f));
                    break;
                }
            }
        }
    }

    private boolean hasNonAirBlocks(Level world, BlockPos center, int high, int TargetRadius) {
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

    private int DestroyBlockJudgmentCenter(Level world, BlockPos center, int TargetRadius) {
        float hardnessSum = 0.0f;

        for (int dx = -2; dx <= 2; dx++) {
            for (int dz = -2; dz <= 2; dz++) {
                BlockPos pos = center.offset(dx, 0, dz);
                BlockState state = world.getBlockState(pos);
                if (state.getBlock() == Blocks.BEDROCK) return 1000000000;
                if (!state.isAir()) {
                    float hardness = state.getDestroySpeed(world, pos);
                    if (hardness > 0) hardnessSum += hardness;
                }
            }
        }

        int innerRad = (int) (TargetRadius * 0.5f);
        int outerRad = (int) (TargetRadius * 0.7f);
        int innerRadSq = (innerRad * innerRad);
        int outerRadSq = (outerRad * outerRad);

        for (int dx = -(int) outerRad; dx <= outerRad; dx++) {
            for (int dz = -(int) outerRad; dz <= outerRad; dz++) {
                int distSq = dx * dx + dz * dz;
                BlockPos pos = center.offset(dx, 0, dz);

                if (world.getBlockState(pos).getBlock() == Blocks.BEDROCK) return 1000000000;
                if (distSq <= innerRadSq) world.destroyBlock(pos, false);
                else if (distSq <= outerRadSq)
                    if (world.random.nextInt(100) < 70) world.destroyBlock(pos, false);
            }
        }

        return (int) hardnessSum;
    }

    private void consumeSource(Level world, BlockPos center) {
        setNeedsSource(true);
        while (sourceCostNeeded > 0) {
            int amountToTake = Math.min(sourceCostNeeded, 5000);
            if (SourceUtil.takeSource(center, world, 6, amountToTake) == null) return;
            sourceCostNeeded -= amountToTake;
        }
        setNeedsSource(false);
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
