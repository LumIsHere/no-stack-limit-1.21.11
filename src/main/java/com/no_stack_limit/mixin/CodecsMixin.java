package com.no_stack_limit.mixin;

import net.minecraft.util.ExtraCodecs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ExtraCodecs.class)
public abstract class CodecsMixin {

    @ModifyVariable(
            method = "intRange",
            at = @At("HEAD"),
            ordinal = 1, // Targets the second integer argument (max)
            argsOnly = true
    )
    private static int noStackLimit$uncapRangedInt(int max) {
        if (max == 99) {
            return Integer.MAX_VALUE;
        }
        return max;
    }
}
