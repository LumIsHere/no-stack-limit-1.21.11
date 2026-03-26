package com.no_stack_limit.mixin.client;

import com.no_stack_limit.StackCountFormatter;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix3x2fStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiGraphics.class)
public abstract class DrawContextMixin {
    private static final float NO_STACK_LIMIT$COUNT_SCALE = 0.7F;

    @Shadow
    public abstract void drawString(Font textRenderer, String text, int x, int y, int color, boolean shadow);

    @Shadow
    public abstract Matrix3x2fStack pose();

    @Inject(method = "renderItemCount", at = @At("HEAD"), cancellable = true)
    private void noStackLimit$drawScaledCount(Font textRenderer, ItemStack stack, int x, int y, String stackCountText, CallbackInfo ci) {
        String countText = noStackLimit$getCountText(stack, stackCountText);
        if (countText == null) {
            return;
        }

        float scale = NO_STACK_LIMIT$COUNT_SCALE;
        float scaledWidth = textRenderer.width(countText) * scale;
        float drawX = x + 17 - scaledWidth;
        float drawY = y + 17 - textRenderer.lineHeight * scale;

        this.pose().pushMatrix();
        this.pose().scale(scale, scale);
        this.drawString(textRenderer, countText, Math.round(drawX / scale), Math.round(drawY / scale), -1, true);
        this.pose().popMatrix();
        ci.cancel();
    }

    private static String noStackLimit$getCountText(ItemStack stack, String stackCountText) {
        if (StackCountFormatter.shouldCompact(stack.getCount())) {
            return StackCountFormatter.formatCompact(stack.getCount());
        }
        if (stackCountText != null) {
            return stackCountText;
        }
        if (stack.getCount() != 1) {
            return Integer.toString(stack.getCount());
        }
        return null;
    }
}

