package com.no_stack_limit.mixin.client;

import com.no_stack_limit.TooltipHelper;
import java.util.List;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Screen.class)
public abstract class ScreenMixin {
    @Inject(method = "getTooltipFromItem", at = @At("RETURN"), cancellable = true)
    private static void noStackLimit$appendExactCount(MinecraftClient client, ItemStack stack, CallbackInfoReturnable<List<Text>> cir) {
        cir.setReturnValue(TooltipHelper.appendExactCount(cir.getReturnValue(), stack));
    }
}
