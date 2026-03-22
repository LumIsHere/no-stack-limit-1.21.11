package com.no_stack_limit.mixin;

import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.dynamic.Codecs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DataComponentTypes.class)
public abstract class DataComponentTypesMixin {
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Inject(method = "method_58570", at = @At("HEAD"), cancellable = true)
    private static void noStackLimit$expandMaxStackSizeComponent(ComponentType.Builder builder, CallbackInfoReturnable<ComponentType.Builder> cir) {
        cir.setReturnValue(builder.codec(Codecs.rangedInt(1, Integer.MAX_VALUE)).packetCodec(PacketCodecs.VAR_INT));
    }
}
