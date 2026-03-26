package com.no_stack_limit.mixin;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.util.ExtraCodecs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.UnaryOperator;

@Mixin(DataComponents.class)
public abstract class DataComponentTypesMixin {
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Redirect(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/core/component/DataComponents;register(Ljava/lang/String;Ljava/util/function/UnaryOperator;)Lnet/minecraft/core/component/DataComponentType;"
            )
    )
    private static <T> DataComponentType<T> noStackLimit$expandMaxStackSizeComponent(String id, UnaryOperator<DataComponentType.Builder<T>> operator) {
        if ("max_stack_size".equals(id)) {
            UnaryOperator expandedOperator = (UnaryOperator<DataComponentType.Builder>) builder ->
                    builder.persistent(ExtraCodecs.intRange(1, Integer.MAX_VALUE))
                            .networkSynchronized(ByteBufCodecs.VAR_INT);
            return noStackLimit$invokeRegister(id, expandedOperator);
        }

        return noStackLimit$invokeRegister(id, operator);
    }

    @Invoker("register")
    private static <T> DataComponentType<T> noStackLimit$invokeRegister(String id, UnaryOperator<DataComponentType.Builder<T>> operator) {
        throw new AssertionError();
    }
}
