package com.arsmeteorites.arsmeteorites.common.explode;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public final class SphereExplosion {

    private final BlockPos center;
    private final Level level;
    private final int radius;

    private SphereExplosion(BlockPos center, Level level, int radius) {
        this.center = center;
        this.level = level;
        this.radius = radius;
        int x = center.getX();
        int y = center.getY();
        int z = center.getZ();

        if (this.level.isClientSide) {
            float soundPitch = (1.0f + (this.level.random.nextFloat() - this.level.random.nextFloat()) * 0.2f) * 0.7f;
            this.level.playLocalSound(x, y, z, SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 4.0f, soundPitch, false);
        }

        this.level.gameEvent(null, GameEvent.EXPLODE, new Vec3(x, y, z));
        float f2 = this.radius * 2.0f;
        List<Entity> entities = this.level.getEntities(null, new AABB(
                Mth.floor(x - f2 - 1.0),
                Mth.floor(y - f2 - 1.0),
                Mth.floor(z - f2 - 1.0),
                Mth.floor(x + f2 + 1.0),
                Mth.floor(y + f2 + 1.0),
                Mth.floor(z + f2 + 1.0)));
        Vec3 explosionPosition = new Vec3(x, y, z);

        double f2Squared = f2 * f2;
        double f2Inverse = 1.0 / f2;

        for (Entity entity : entities) {
            double distanceToSqr = entity.distanceToSqr(explosionPosition);
            if (entity.ignoreExplosion() || distanceToSqr > f2Squared) {
                continue;
            }

            double knockbackFactor = 1.0 - distanceToSqr * f2Inverse;
            int damage = (int) ((knockbackFactor * knockbackFactor + knockbackFactor) / 2.0 * 7.0 * f2 + 1.0);
            entity.hurt(entity.damageSources().genericKill(), damage);
            Vec3 direction = new Vec3(
                    (entity.getX() - x),
                    (entity.getY() - y),
                    (entity.getZ() - z)).normalize().scale(knockbackFactor);

            entity.setDeltaMovement(entity.getDeltaMovement().add(direction));
        }
        level.addFreshEntity(new TaskEntity(level, center, this::breakBlocksInExplosionArea));
    }

    private void breakBlocksInExplosionArea(Entity entity) {
        int currentLayer = Math.min(radius, 19) + entity.tickCount;
        if (currentLayer > radius) {
            entity.discard();
            return;
        }

        int surfaceLayerEnd = currentLayer + 1;
        int surfaceLayerEndSquared = surfaceLayerEnd * surfaceLayerEnd;
        int surfaceLayerStartSquared = currentLayer > 20 ? (currentLayer - 1) * (currentLayer - 1) : 0;

        for (int x = -surfaceLayerEnd; x <= surfaceLayerEnd; x++) {
            int xSquared = x * x;
            for (int y = -surfaceLayerEnd; y <= surfaceLayerEnd; y++) {
                int ySquared = y * y;
                for (int z = -surfaceLayerEnd; z <= surfaceLayerEnd; z++) {
                    int zSquared = z * z;
                    int distanceSquared = xSquared + ySquared + zSquared;
                    if (distanceSquared < surfaceLayerStartSquared || distanceSquared > surfaceLayerEndSquared) continue;
                    BlockPos pos = center.offset(x, y, z);
                    BlockState state = level.getBlockState(pos);
                    if (state.isAir() || (state.getBlock() == Blocks.BEDROCK)) continue;
                    level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                }
            }
        }
    }

    public static void explosion(BlockPos center, Level level, int radius) {
        new SphereExplosion(center, level, radius);
    }
}
