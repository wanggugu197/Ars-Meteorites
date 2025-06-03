package com.arsmeteorites.arsmeteorites.common.explode;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

import static com.arsmeteorites.arsmeteorites.common.explode.EntityRegistry.DELAYED_BLOCK_BREAKER;

public final class TaskEntity extends Entity {

    private Consumer<TaskEntity> consumer;

    public TaskEntity(Level level, BlockPos pos, Consumer<TaskEntity> consumer) {
        this(DELAYED_BLOCK_BREAKER.get(), level);
        setPos(pos.getX(), pos.getY(), pos.getZ());
        this.consumer = consumer;
    }

    public TaskEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void tick() {
        if (consumer == null) {
            discard();
        } else {
            consumer.accept(this);
        }
    }

    @Override
    public void kill() {}

    @Override
    public boolean isInvulnerableTo(@NotNull DamageSource source) {
        return true;
    }

    @Override
    protected void defineSynchedData() {}

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag compound) {}

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag compound) {}
}
