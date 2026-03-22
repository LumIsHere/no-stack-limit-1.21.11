package com.no_stack_limit.mixin.client;

import com.no_stack_limit.StackCountFormatter;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DrawContext.class)
public abstract class DrawContextMixin {
    @Shadow
    public abstract void drawText(TextRenderer textRenderer, String text, int x, int y, int color, boolean shadow);

    @Inject(method = "drawStackCount", at = @At("HEAD"), cancellable = true)
    private void noStackLimit$drawCompactCount(TextRenderer textRenderer, ItemStack stack, int x, int y, String stackCountText, CallbackInfo ci) {
        if (!StackCountFormatter.shouldCompact(stack.getCount())) {
            return;
        }

        String compactCount = StackCountFormatter.formatCompact(stack.getCount());
        this.drawText(textRenderer, compactCount, x + 17 - textRenderer.getWidth(compactCount), y + 9, -1, true);
        ci.cancel();
    }
}
