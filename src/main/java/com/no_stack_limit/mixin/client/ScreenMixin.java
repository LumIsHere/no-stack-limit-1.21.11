package com.no_stack_limit.mixin.client;

import com.no_stack_limit.NoStackLimitClient;
import com.no_stack_limit.StackCountFormatter;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Screen.class)
public abstract class ScreenMixin {
    @Inject(method = "getTooltipFromItem", at = @At("RETURN"), cancellable = true)
    private static void noStackLimit$appendExactCount(MinecraftClient client, ItemStack stack, CallbackInfoReturnable<List<Text>> cir) {
        if (!StackCountFormatter.shouldCompact(stack.getCount()) || !NoStackLimitClient.shouldShowExactCount()) {
            return;
        }

        List<Text> tooltip = new ArrayList<>(cir.getReturnValue());
        tooltip.add(Text.translatable("tooltip.no_stack_limit.exact_count", StackCountFormatter.formatExact(stack.getCount()))
                .formatted(Formatting.GRAY));
        cir.setReturnValue(tooltip);
    }
}
