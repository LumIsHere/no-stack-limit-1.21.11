package com.no_stack_limit.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackPacketValidationMixin {
    @Inject(method = "createExtraValidatingPacketCodec", at = @At("HEAD"), cancellable = true)
    private static void noStackLimit$skipExtraPacketRoundTripValidation(PacketCodec<RegistryByteBuf, ItemStack> packetCodec, CallbackInfoReturnable<PacketCodec<RegistryByteBuf, ItemStack>> cir) {
        cir.setReturnValue(packetCodec);
    }
}
