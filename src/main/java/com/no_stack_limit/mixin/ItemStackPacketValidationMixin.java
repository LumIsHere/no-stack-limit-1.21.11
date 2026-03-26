package com.no_stack_limit.mixin;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackPacketValidationMixin {
    @Inject(method = "validatedStreamCodec", at = @At("HEAD"), cancellable = true)
    private static void noStackLimit$skipExtraPacketRoundTripValidation(StreamCodec<RegistryFriendlyByteBuf, ItemStack> packetCodec, CallbackInfoReturnable<StreamCodec<RegistryFriendlyByteBuf, ItemStack>> cir) {
        cir.setReturnValue(packetCodec);
    }
}
