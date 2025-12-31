package com.arsmeteorites.arsmeteorites.common.explode;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;

import com.hollingsworth.arsnouveau.setup.registry.ModEntities;

public class EntityRegistry {

    public static final DeferredHolder<EntityType<?>, EntityType<TaskEntity>> DELAYED_BLOCK_BREAKER = ModEntities.ENTITIES
            .register("delayed_block_breaker",
                    () -> EntityType.Builder.<TaskEntity>of(
                            TaskEntity::new,
                            MobCategory.MISC)
                            .sized(0.1f, 0.1f)
                            .clientTrackingRange(0)
                            .build("delayed_block_breaker"));

    public static void init() {
        // This empty method forces the static initialization
    }
}
