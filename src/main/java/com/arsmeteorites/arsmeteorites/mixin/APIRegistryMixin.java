package com.arsmeteorites.arsmeteorites.mixin;

import com.arsmeteorites.arsmeteorites.common.ConjureMeteoritesRitual;

import com.hollingsworth.arsnouveau.setup.registry.APIRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(APIRegistry.class)
public class APIRegistryMixin {

    @Inject(method = "setup()V", at = @At("TAIL"), remap = false)
    private static void addMeteoriteRitual(CallbackInfo ci) {
        APIRegistry.registerRitual(new ConjureMeteoritesRitual());
    }
}
