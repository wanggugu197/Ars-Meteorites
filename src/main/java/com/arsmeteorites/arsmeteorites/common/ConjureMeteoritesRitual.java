package com.arsmeteorites.arsmeteorites.common;

import com.arsmeteorites.arsmeteorites.ArsMeteorites;
import com.arsmeteorites.arsmeteorites.MeteoriteRitualConfig;
import com.arsmeteorites.arsmeteorites.common.explode.SphereExplosion;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.FullChunkStatus;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.lighting.LightEngine;

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
    private long sourceCostNeeded = 0L;
    private BlockPos center = new BlockPos(0, 0, 0);
    private int checkTime = 0;
    private boolean isCenterCheck = false;
    private long destructionValue = 0L;

    private final int[] Number_of_blocks = {
            1, 6, 26, 90, 134, 258, 410, 494, 690, 962,
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
            243294, 250082, 250002, 255290, 258878, 262170, 267410, 269886, 272522, 277586,
            281730, 282902, 287970, 292706, 297246, 299570, 303050, 307458, 311318, 317346,
            318122, 323670, 328970, 332138, 334662, 342242, 343074, 347810, 352494, 355874,
            362066, 366726, 368114, 374322, 377978, 383462, 386418, 392114, 393342, 401018,
            405498, 410318, 412058, 420186, 423410, 427374, 433034, 436722, 443270, 445850,
            450114, 454490, 462678, 464114, 471506, 475830, 480290, 486330, 487658, 495846,
            499490, 506666, 509430, 515138, 521418, 524054, 533178, 536018, 538682, 546846,
            552122, 556266, 560678, 569130, 573938, 577154, 582006, 590858, 592506, 603014,
            602042, 611850, 617462, 621522, 627674, 634722, 638678, 642914, 651498, 656198,
            663018, 667754, 671370, 680342, 686546, 689658, 696302, 704586, 706610, 712758,
            722042, 727082, 733626, 740918, 743970, 753482, 755366, 762642, 771458, 775146,
            780830, 789378, 795554, 800870, 805050, 818234, 818082, 826910, 832986, 840530,
            844382, 855450, 857186, 864726, 873818, 878610, 888650, 890318, 897210, 909458,
            913782, 915122, 928898, 934458, 939782, 945378, 952682, 964038, 963266, 977858,
            979302, 989594, 996402, 1001714, 1011462, 1016834, 1025978, 1028286, 1038890, 1044690,
            1054514, 1062182, 1065354, 1076354, 1085214, 1087658, 1096914, 1106594, 1110686, 1121130,
            1125074, 1135494, 1138850, 1156122, 1153118, 1164650, 1175466, 1178474, 1191126, 1192274,
            1204626, 1211630, 1219994, 1227714, 1233626, 1249206, 1246610, 1258754, 1266558, 1274690,
            1280586, 1291502, 1298010, 1310642, 1314026, 1320270, 1332770, 1342554, 1345502, 1357794,
            1365578, 1373210, 1375902, 1390130, 1399554, 1402622, 1415106, 1423826, 1429658, 1443078,
            1446170, 1460706, 1464974, 1471082, 1485690, 1490630, 1502058, 1505714, 1514970, 1529126,
            1535426, 1544874, 1548254, 1563426, 1572746, 1575282, 1591190, 1598138, 1604034, 1614542,
            1620354, 1638338, 1636958, 1658106, 1656386, 1669290, 1683398, 1681698, 1699850, 1707902,
            1717698, 1723178, 1730346, 1746566, 1753554, 1765610, 1768142, 1778394, 1796666, 1796454,
            1810106, 1820466, 1824218, 1841174, 1848066, 1861058, 1868790, 1874474, 1886090, 1895802,
            1911086, 1911642, 1927082, 1939038, 1942154, 1954322, 1966602, 1977638, 1981050, 2002586,
            2000526, 2016290, 2030042, 2031822, 2046626, 2059314, 2065082, 2072598, 2084378, 2104154,
            2103750, 2119394, 2127786, 2138402, 2147726, 2154786, 2172770, 2179998, 2185298, 2206146,
            2208062, 2224514, 2230794, 2249666, 2249670, 2262986, 2280978, 2285150, 2295914, 2308842,
            2313842, 2326998, 2337698, 2353434, 2362694, 2379434, 2372898, 2396018, 2408910, 2412578,
            2428970, 2442774, 2445698, 2460474, 2468510, 2482938, 2497178, 2505602, 2517030, 2517914,
            2547930, 2551598, 2559690, 2575850, 2581706, 2598270, 2603114, 2621322, 2630630, 2643770,
            2650626, 2662526, 2677650, 2684978, 2702106, 2713070, 2721674, 2737554, 2742686, 2761362,
            2770202, 2780274, 2794190, 2806610, 2815674, 2830430, 2841282, 2855474, 2857758, 2878490,
            2887850, 2901858, 2916398, 2927850, 2937410, 2949326, 2961306, 2971826, 2987610, 3000782,
            3013746, 3018242, 3038126, 3045978, 3065354, 3072186, 3082574, 3096354, 3113810, 3120374,
            3132354, 3154802, 3151182, 3176282, 3185082, 3197066, 3216974, 3219930, 3237866, 3250758,
    };

    @Override
    public void onStart(@Nullable Player player) {
        super.onStart(player);
        Level world = getWorld();
        if (world == null) return;
        if (world.isClientSide) return;

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

        int itemCount = map.getInt(currentMeteoriteType.consumeitem());
        TargetRadius = minRadius;
        while (itemCount > 0) {
            int cost = getCostForRadius(TargetRadius);
            if (cost == 0 || itemCount < cost) break;
            itemCount -= cost;
            TargetRadius++;
        }
        TargetRadius = Math.min(TargetRadius, maxRadius);

        ZOffset += map.getInt(BlockRegistry.BOMBEGRANTE_POD.get().asItem());
        ZOffset -= map.getInt(BlockRegistry.FROSTAYA_POD.get().asItem());
        XOffset += map.getInt(BlockRegistry.MENDOSTEEN_POD.get().asItem());
        XOffset -= map.getInt(BlockRegistry.BASTION_POD.get().asItem());

        checkTime = world.getHeight();
        isCenterCheck = false;
        center = Objects.requireNonNull(getPos()).offset(XOffset, TargetRadius << 1, ZOffset);
    }

    private int getCostForRadius(int radius) {
        if (radius < 100) return 1;
        if (radius < 200) return 2;
        if (radius < 250) return 4;
        if (radius < 300) return 6;
        if (radius < 350) return 8;
        if (radius < 375) return 10;
        if (radius < 400) return 14;
        if (radius < 425) return 18;
        if (radius < 450) return 22;
        if (radius < 470) return 28;
        if (radius < 490) return 36;
        if (radius < 510) return 46;
        return 0;
    }

    @Override
    protected void tick() {
        Level world = getWorld();

        if (world != null && !world.isClientSide) {
            if (world.getGameTime() % (TargetRadius / 25 + 1) == 0 && !isCenterCheck) {
                if (checkTime <= 0) setFinished();
                else getCenter(world, TargetRadius);
                return;
            }

            if (CurrentRadius >= TargetRadius) setFinished();

            if (world.getGameTime() % (((long) CurrentRadius * CurrentRadius / 500) + 1) == 0 && isCenterCheck && sourceCostNeeded == 0) {
                generateMeteoriteLayer(world, center, CurrentRadius, (double) CurrentRadius / TargetRadius);
                sourceCostNeeded = Math.max(1, (long) (Number_of_blocks[CurrentRadius + 1] * SourceCost));
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
        if (world.isClientSide()) return;
        ServerLevel serverLevel = (ServerLevel) world;

        if (CurrentRadius == 0) {
            fastSetBlock(serverLevel, center, currentMeteoriteType.meteorites()[0].defaultBlockState());
            return;
        }

        int radiusSq = CurrentRadius * CurrentRadius;
        int innerRadiusSq = (CurrentRadius - 1) * (CurrentRadius - 1);

        if (model == 0) {
            iterateSphereBlocks(center, CurrentRadius, radiusSq, innerRadiusSq, (pos) -> fastSetBlock(serverLevel, pos, getRandomMeteoriteBlock()));
        } else if (model == 1) {
            BlockState MeteoriteBlock = getFixedBlockForLayer(proportion);
            iterateSphereBlocks(center, CurrentRadius, radiusSq, innerRadiusSq, (pos) -> fastSetBlock(serverLevel, pos, MeteoriteBlock));
        } else if (model == 2) {
            BlockState MeteoriteBlock = getRandomMeteoriteBlock();
            iterateSphereBlocks(center, CurrentRadius, radiusSq, innerRadiusSq, (pos) -> fastSetBlock(serverLevel, pos, MeteoriteBlock));
        } else if (model == 3) {
            int Layer = getLayeredMeteoriteBlockA(proportion);
            iterateSphereBlocks(center, CurrentRadius, radiusSq, innerRadiusSq, (pos) -> fastSetBlock(serverLevel, pos, getLayeredMeteoriteBlockB(Layer)));
        } else if (model == 4) {
            BlockState MeteoriteBlock = getLayeredMeteoriteBlockB(getLayeredMeteoriteBlockA(proportion));
            iterateSphereBlocks(center, CurrentRadius, radiusSq, innerRadiusSq, (pos) -> fastSetBlock(serverLevel, pos, MeteoriteBlock));
        }
    }

    private void iterateSphereBlocks(BlockPos center, int CurrentRadius, int radiusSq, int innerRadiusSq, java.util.function.Consumer<BlockPos> blockAction) {
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
            for (int i = 0; i < (500 / TargetRadius == 0 ? 1 : 500 / TargetRadius); i++) {
                if (checkTime == world.getHeight()) {
                    center = center.atY(world.getMaxBuildHeight());
                    destructionValue = ((long) TargetRadius * TargetRadius * TargetRadius / 5);
                    checkTime--;
                }
                destructionValue -= DestroyBlockJudgmentCenter(world, center, TargetRadius);
                if (destructionValue > 0L) {
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
        if (world.isClientSide()) return 0;
        ServerLevel serverLevel = (ServerLevel) world;
        float hardnessSum = 0.0f;

        for (int dx = -2; dx <= 2; dx++) {
            for (int dz = -2; dz <= 2; dz++) {
                BlockPos pos = center.offset(dx, 0, dz);
                BlockState state = getBlockStateLikeSetBlock(serverLevel, pos);
                if (state == null) continue;
                if (isUnbreakableBlock(state.getBlock())) return 1000000000;
                if (!state.isAir()) {
                    float hardness = state.getDestroySpeed(serverLevel, pos);
                    if (hardness > 0) hardnessSum += hardness;
                }
            }
        }

        int innerRad = (int) (TargetRadius * 0.5f);
        int outerRad = (int) (TargetRadius * 0.7f);
        int innerRadSq = innerRad * innerRad;
        int outerRadSq = outerRad * outerRad;

        for (int dx = -outerRad; dx <= outerRad; dx++) {
            for (int dz = -outerRad; dz <= outerRad; dz++) {
                int distSq = dx * dx + dz * dz;
                if (distSq > outerRadSq) continue;
                BlockPos pos = center.offset(dx, 0, dz);
                BlockState state = getBlockStateLikeSetBlock(serverLevel, pos);
                if (state == null) continue;
                if (isUnbreakableBlock(state.getBlock())) return 1000000000;
                if (distSq <= innerRadSq || serverLevel.random.nextInt(100) < 70) {
                    fastRemoveBlock(serverLevel, pos);
                }
            }
        }

        return (int) hardnessSum;
    }

    @Nullable
    private BlockState getBlockStateLikeSetBlock(ServerLevel serverLevel, BlockPos pos) {
        int y = pos.getY();
        if (serverLevel.isOutsideBuildHeight(y)) return null;
        LevelChunk chunk = serverLevel.getChunkAt(pos);
        int sectionIndex = chunk.getSectionIndex(y);
        LevelChunkSection section = chunk.getSection(sectionIndex);
        int localX = pos.getX() & 15;
        int localY = y & 15;
        int localZ = pos.getZ() & 15;
        return section.getBlockState(localX, localY, localZ);
    }

    private boolean isUnbreakableBlock(Block block) {
        return block == Blocks.BEDROCK || block == Blocks.REINFORCED_DEEPSLATE || block == Blocks.END_PORTAL_FRAME;
    }

    private void consumeSource(Level world, BlockPos center) {
        setNeedsSource(true);
        while (sourceCostNeeded > 0) {
            int amountToTake = (int) Math.min(sourceCostNeeded, 5000);
            if (SourceUtil.takeSource(center, world, 6, amountToTake) == null) return;
            sourceCostNeeded -= amountToTake;
        }
        setNeedsSource(false);
    }

    static void fastSetBlock(ServerLevel level, BlockPos pos, BlockState newState) {
        int y = pos.getY();
        if (level.isOutsideBuildHeight(y)) return;
        LevelChunk chunk = level.getChunkAt(pos);
        int sectionIndex = chunk.getSectionIndex(y);
        LevelChunkSection section = chunk.getSection(sectionIndex);
        int localX = pos.getX() & 15;
        int localY = y & 15;
        int localZ = pos.getZ() & 15;
        BlockState oldState = section.getBlockState(localX, localY, localZ);
        if (!oldState.isAir() || newState.isAir()) return;
        section.setBlockState(localX, localY, localZ, newState);
        Heightmap.Types[] heightmapTypes = { Heightmap.Types.MOTION_BLOCKING, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Heightmap.Types.OCEAN_FLOOR, Heightmap.Types.WORLD_SURFACE };
        for (Heightmap.Types type : heightmapTypes) chunk.getOrCreateHeightmapUnprimed(type).update(localX, y, localZ, newState);
        if (LightEngine.hasDifferentLightProperties(level, pos, oldState, newState)) {
            chunk.getSkyLightSources().update(level, localX, localY, localZ);
            level.getChunkSource().getLightEngine().checkBlock(pos);
        }
        oldState.onRemove(level, pos, newState, false);
        if (newState.hasBlockEntity() && newState.getBlock() instanceof EntityBlock entityBlock) {
            BlockEntity newBE = entityBlock.newBlockEntity(pos, newState);
            if (newBE != null) {
                level.setBlockEntity(newBE);
                chunk.setBlockEntity(newBE);
            }
        }
        if (chunk.getFullStatus().isOrAfter(FullChunkStatus.BLOCK_TICKING)) {
            level.getChunkSource().blockChanged(pos);
        }
        chunk.setUnsaved(true);
    }

    static void fastRemoveBlock(ServerLevel level, BlockPos pos) {
        int i = pos.getY();
        if (level.isOutsideBuildHeight(i)) return;
        LevelChunk levelchunk = level.getChunkAt(pos);
        LevelChunkSection levelchunksection = levelchunk.getSection(levelchunk.getSectionIndex(i));
        if (!levelchunksection.hasOnlyAir()) {
            if (levelchunk.getBlockState(pos).is(Blocks.BEDROCK)) return;
            if (levelchunk.getBlockState(pos).getBlock().equals(BlockRegistry.RITUAL_BLOCK.get())) return;
            var state = Blocks.AIR.defaultBlockState();
            int j = pos.getX() & 15;
            int k = i & 15;
            int l = pos.getZ() & 15;
            var old = levelchunksection.setBlockState(j, k, l, state);
            if (old.isAir()) return;
            Heightmap.Types[] heightmapTypes = { Heightmap.Types.MOTION_BLOCKING, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Heightmap.Types.OCEAN_FLOOR, Heightmap.Types.WORLD_SURFACE };
            for (Heightmap.Types type : heightmapTypes) levelchunk.getOrCreateHeightmapUnprimed(type).update(j, i, l, state);
            if (LightEngine.hasDifferentLightProperties(level, pos, old, state)) {
                levelchunk.getSkyLightSources().update(level, j, i, l);
                level.getChunkSource().getLightEngine().checkBlock(pos);
            }
            if (levelchunksection.hasOnlyAir()) level.getChunkSource().getLightEngine().updateSectionStatus(pos, true);
            old.onRemove(level, pos, Blocks.AIR.defaultBlockState(), false);
            if (old.hasBlockEntity()) level.removeBlockEntity(pos);
            var fullStatus = levelchunk.getFullStatus();
            if (fullStatus.isOrAfter(FullChunkStatus.BLOCK_TICKING)) {
                level.getChunkSource().blockChanged(pos);
            }
            levelchunk.setUnsaved(true);
        }
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

    @Override
    @Deprecated(since = "4.11.0", forRemoval = true)
    public boolean canBeTraded() {
        return false;
    }
}
