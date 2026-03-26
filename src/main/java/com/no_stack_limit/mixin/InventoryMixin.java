package com.no_stack_limit.mixin;

import com.no_stack_limit.NoStackLimit;
import net.minecraft.world.Container;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Container.class)
public interface InventoryMixin {
    @Inject(method = "getMaxStackSize()I", at = @At("HEAD"), cancellable = true)
    private void noStackLimit$allowHugeStacks(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(NoStackLimit.MAX_STACK_SIZE);
    }
}
