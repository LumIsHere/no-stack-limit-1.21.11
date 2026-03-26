package com.no_stack_limit.mixin;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.util.ExtraCodecs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DataComponents.class)
public abstract class DataComponentTypesMixin {
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Inject(method = "method_58570", at = @At("HEAD"), cancellable = true)
    private static void noStackLimit$expandMaxStackSizeComponent(DataComponentType.Builder builder, CallbackInfoReturnable<DataComponentType.Builder> cir) {
        cir.setReturnValue(builder.persistent(ExtraCodecs.intRange(1, Integer.MAX_VALUE)).networkSynchronized(ByteBufCodecs.VAR_INT));
    }
}
